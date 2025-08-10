export type ApiResponse<T> = {
  success: boolean
  message: string
  data: T
}

export type PageInfo = {
  size: number
  number: number
  totalElements: number
  totalPages: number
}

export type Paged<T> = {
  content: T[]
  page: PageInfo
}

export type User = {
  id?: string
  createdAt?: string | null
  lastUpdatedAt?: string | null
  username: string
  name: string
  email: string
}

export type Book = {
  id?: string
  createdAt?: string | null
  lastUpdatedAt?: string | null
  title: string
  author: string
  isbn: string
  publicationYear: number
  availableQuantity: number
}

export type Borrowing = {
  id: string
  createdAt: string
  lastUpdatedAt: string | null
  user: User
  book: Book
  expectedReturnDate: string
  actualReturnDate: string | null
}

export type CreateBookBody = {
  title: string
  author: string
  isbn: string
  pubYear: number
  quantity: number
}

export type UpdateBookBody = Partial<{
  title: string
  author: string
  isbn: string
  pubYear: number
  quantity: number
}>

export type SearchBookBody = Partial<{
  author: string
  title: string
  isbn: string
  pubYear: number
}>
