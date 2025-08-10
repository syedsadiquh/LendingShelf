<div align="center">

# Lending Shelf

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-brightgreen?style=for-the-badge&logo=spring)
![Next.js](https://img.shields.io/badge/Next.js-15.0.3-black?style=for-the-badge&logo=next.js)
![TypeScript](https://img.shields.io/badge/TypeScript-5.5.4-blue?style=for-the-badge&logo=typescript)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue?style=for-the-badge&logo=postgresql)
![Git](https://img.shields.io/badge/Git-Version%20Control-red?style=for-the-badge&logo=git)
![MIT](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

</div>

A modern, full-stack book lending management system that allows users to manage books, users, and borrowing transactions efficiently. Built with Spring Boot backend and Next.js frontend.

## Live Demo

- **Frontend**: [https://lendingshelf.vercel.app](https://lendingshelf.vercel.app)
- **Backend**: Deployed on Render
- **Database**: PostgreSQL hosted on Koyeb

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Setup & Deployment](#setup--deployment)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [Testing](#testing)
- [License](#license)
- [Author](#author)
- [Acknowledgments](#acknowledgments)
- [Support](#support)

## Features

### Backend Features

#### Book Management
- **Create Books**: Add new books with title, author, ISBN, publication year, and available quantity
- **Search Books**: Advanced search functionality with pagination and sorting
- **Update Books**: Modify book details and inventory
- **Delete Books**: Remove books from the system
- **Find by ISBN**: Quick book lookup using ISBN
- **Inventory Management**: Track available quantities for lending

#### User Management
- **User Registration**: Create new user accounts with username, name, and email
- **User Profiles**: Manage user information and preferences
- **Update Users**: Modify user details including username changes
- **User Lookup**: Find users by ID or username
- **User Deletion**: Remove user accounts from the system

#### Borrowing System
- **Borrow Books**: Create borrowing records with expected return dates
- **Return Books**: Process book returns and update inventory
- **Active Borrowings**: Track currently borrowed books by user
- **Borrowing History**: View all borrowing transactions with filtering
- **Due Date Management**: Monitor expected vs actual return dates
- **Inventory Updates**: Automatic quantity adjustments on borrow/return

#### System Features
- **RESTful API**: Clean, well-structured REST endpoints
- **Data Validation**: Input validation using Spring Boot Validation
- **Error Handling**: Comprehensive global exception handling
- **Pagination**: Efficient data retrieval with pagination support
- **Sorting**: Flexible sorting options for all list endpoints
- **CORS Support**: Cross-origin resource sharing configuration
- **Database Auditing**: Automatic timestamp tracking (created/updated)
- **UUID Primary Keys**: Secure, unique identifiers for all entities

### Frontend Features

#### User Interface
- **Modern Design**: Clean, responsive UI built with Tailwind CSS
- **Dark/Light Mode**: Theme switching with next-themes
- **Accessible Components**: Radix UI primitives for accessibility
- **Mobile Responsive**: Optimized for all device sizes
- **Interactive Tables**: Data tables with sorting and pagination
- **Form Validation**: Client-side validation for better UX

#### Management Panels
- **Books Panel**: Comprehensive book management interface
- **Users Panel**: User administration and profile management
- **Borrowings Panel**: Lending transaction management
- **Dashboard**: Overview of system statistics and activities

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.5.0
- **Language**: Java 21
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Maven
- **Validation**: Spring Boot Validation
- **Documentation**: Postman Collection
- **Utilities**: Lombok for boilerplate reduction

### Frontend
- **Framework**: Next.js 15.0.3
- **Language**: TypeScript
- **UI Library**: React 19.0.0
- **Styling**: Tailwind CSS 4.0.0
- **Components**: Radix UI primitives
- **Icons**: Lucide React
- **Themes**: next-themes
- **Build Tool**: Turbo (Next.js)

### Deployment & Infrastructure
- **Backend Hosting**: Render
- **Frontend Hosting**: Vercel
- **Database**: Koyeb (PostgreSQL)
- **Version Control**: Git

## Setup & Deployment

### Prerequisites & Requirements

- **Java 21** or higher
- **Node.js 20.x** or higher
- **PostgreSQL** database
- **Maven** (for backend)
- **npm** (for frontend)

### Local Development Setup

#### 1. Clone & Initial Setup
```bash
git clone <repository-url>
cd LendingShelf
```

#### 2. Database Configuration
Create a PostgreSQL database named `lending_shelf` and configure:

**Backend Configuration** (`book-lending-system/src/main/resources/application.yml`):
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/lending_shelf
    username: postgres
    password: your_password
```

**Frontend Configuration** (`book-lending-frontend/.env.local`):
```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

#### 3. Backend Setup
```bash
cd book-lending-system
mvn clean install
mvn spring-boot:run
```
Backend will start on `http://localhost:8080`

#### 4. Frontend Setup
```bash
cd ../book-lending-frontend
npm install
npm run dev
```
Frontend will start on `http://localhost:3000`

#### 5. Verify Installation
```bash
curl http://localhost:8080/v1/user/getAllUsers
```

### Production Deployment

#### Environment Variables
Set these environment variables for production:

**Backend (Render/Docker):**
```bash
export SPRING_PROFILES_ACTIVE=prod
export SPRING_DB_URL=your_database_url
export SPRING_DB_USERNAME=your_username
export SPRING_DB_PASSWORD=your_password
```

**Frontend (Vercel):**
```env
NEXT_PUBLIC_API_URL=https://your-backend-url.onrender.com
```

#### Deployment Platforms
- **Backend**: Deploy to Render using Docker service
- **Frontend**: Deploy to Vercel (connects automatically to GitHub)
- **Database**: PostgreSQL hosted on Koyeb

### Docker Setup (Alternative)

For containerized deployment:

```bash
# Backend
cd book-lending-system
docker build -t lending-shelf-backend .
docker run -p 8080:8080 lending-shelf-backend

# Frontend
cd ../book-lending-frontend
docker build -t lending-shelf-frontend .
docker run -p 3000:3000 lending-shelf-frontend
```

## API Documentation

### Base URL & Resources
- **Local**: `http://localhost:8080`
- **Production**: `https://your-render-app.onrender.com` (Of course, it's fake. Deploy on Render using Docker service)
- **Postman Collection**: Import `Book Lending System.postman_collection.json` for complete API documentation with example requests and responses

### API Endpoints

#### User Management

**POST /v1/user/createUser** - Create a new user
```json
{
  "username": "johndoe",
  "name": "John Doe",
  "email": "john.doe@example.com"
}
```

**GET /v1/user/getAllUsers** - Get all users
*No request body required*

**GET /v1/user/findUserById?id={uuid}** - Find user by ID
*No request body required*

**GET /v1/user/findUserByUsername?username={string}** - Find user by username
*No request body required*

**POST /v1/user/updateUser** - Update user information
```json
{
  "username": "johndoe",
  "name": "John Smith",
  "email": "john.smith@example.com"
}
```

**POST /v1/user/updateUsername** - Update username
```json
{
  "oldUsername": "johndoe",
  "newUsername": "johnsmith"
}
```

**DELETE /v1/user/deleteUser?username={string}** - Delete user
*No request body required*

#### Book Management

**POST /v1/book/createBook** - Create a new book
```json
{
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "isbn": "978-0-7432-7356-5",
  "pubYear": 1925,
  "quantity": 5
}
```

**GET /v1/book/getAllBooks** - Get all books (with pagination)
*No request body required. Supports query parameters: page, size, sortBy, ascending*

**GET /v1/book/findBookByIsbn?isbn={string}** - Find book by ISBN
*No request body required*

**GET /v1/book/searchBook** - Search books with filters
```json
{
  "title": "Great",
  "author": "Fitzgerald",
  "isbn": "978-0-7432-7356-5",
  "pubYear": 1925
}
```
*Note: All fields are optional for search filtering*

**POST /v1/book/updateBook?id={uuid}** - Update book information
```json
{
  "title": "The Great Gatsby - Updated Edition",
  "author": "F. Scott Fitzgerald",
  "isbn": "978-0-7432-7356-5",
  "pubYear": 1925,
  "quantity": 3
}
```

**DELETE /v1/book/deleteBook?id={uuid}** - Delete book
*No request body required*

#### Borrowing Management

**POST /v1/borrowing/createBorrowing** - Create a borrowing record
```json
{
  "user_id": "123e4567-e89b-12d3-a456-426614174000",
  "book_id": "987fcdeb-51a2-43d1-b789-123456789abc",
  "expectedReturnDate": "2024-09-15T10:30:00"
}
```
*Note: expectedReturnDate is optional and should be in ISO 8601 format*

**GET /v1/borrowing/getAllBorrowing** - Get all borrowings (with filters)
*No request body required. Supports optional query parameters: username, bookTitle*

**POST /v1/borrowing/returnBorrowing?borrowing_id={uuid}** - Return a borrowed book
*No request body required*

**GET /v1/borrowing/getActiveBorrowing?user_id={uuid}** - Get active borrowings for user
*No request body required*

### Request/Response Format

All API responses follow this structure:
```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": {}
}
```

**Response Fields:**
- `success`: Boolean indicating if the operation was successful
- `message`: Descriptive message about the operation result
- `data`: The actual response data (object, array, or null)

## Project Structure

```
LendingShelf/
├── book-lending-system/          # Spring Boot Backend
│   ├── src/main/java/com/syedsadiquh/lendingshelf/
│   │   ├── controller/           # REST Controllers
│   │   ├── dto/                  # Data Transfer Objects
│   │   ├── models/               # JPA Entities
│   │   ├── repositories/         # Data Access Layer
│   │   ├── service/              # Business Logic
│   │   ├── specifications/       # JPA Specifications
│   │   └── configuration/        # Spring Configuration
│   ├── src/main/resources/       # Configuration Files
│   └── pom.xml                   # Maven Dependencies
├── book-lending-frontend/        # Next.js Frontend
│   ├── app/                      # Next.js App Router
│   ├── components/               # Reusable UI Components
│   ├── features/                 # Feature-specific Components
│   ├── lib/                      # Utility Functions
│   ├── hooks/                    # Custom React Hooks
│   └── package.json              # npm Dependencies
└── Book Lending System.postman_collection.json
```

## Contributing

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Commit your changes**
   ```bash
   git commit -m 'Add some amazing feature'
   ```
4. **Push to the branch**
   ```bash
   git push origin feature/amazing-feature
   ```
5. **Open a Pull Request**

### Development Guidelines

- Follow Java coding conventions for backend
- Use TypeScript for all frontend code
- Write meaningful commit messages
- Add tests for new features
- Update documentation as needed

## Testing

### Backend Testing
```bash
cd book-lending-system
mvn test
```

### Frontend Testing
```bash
cd book-lending-frontend
npm run test
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

**Syed Sadiqu Hussain** - [GitHub Profile](https://github.com/syedsadiquh)

## Acknowledgments

- Spring Boot team for the excellent framework
- Next.js team for the powerful React framework
- Radix UI for accessible component primitives
- Tailwind CSS for utility-first styling
- All open-source contributors who made this project possible

## Support

If you have any questions, or need help with setup, please:

1. Check the [Issues](https://github.com/syedsadiquh/LendingShelf/issues) page
2. Create a new issue if your problem isn't already reported
3. Provide detailed information about your environment and the issue

---

⭐ **Star this repository if you found it helpful!**