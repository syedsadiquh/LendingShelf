# =====================================================================================
# Stage 1: Build the Application using Maven with JDK 21
# This stage uses a full JDK and Maven to build the Spring Boot executable JAR.
# =====================================================================================
FROM eclipse-temurin:21-jdk-jammy as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files first to leverage Docker's layer caching.
# If these files don't change, Docker won't re-download dependencies.
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download all project dependencies. This is a separate layer.
RUN ./mvnw dependency:go-offline

# Copy the rest of the application's source code
COPY src ./src

# Package the application, skipping the tests for a faster build.
# The final JAR will be created in the 'target' directory.
RUN ./mvnw package -DskipTests


# =====================================================================================
# Stage 2: Create the Final, Optimized Runtime Image with JRE 21
# This stage uses a minimal Java Runtime Environment (JRE) for a smaller footprint
# and improved security, as it doesn't contain build tools like Maven or the JDK.
# =====================================================================================
FROM eclipse-temurin:21-jre-jammy

# Set the working directory for the runtime container
WORKDIR /app

# Argument to hold the path to the JAR file from the build stage.
# This makes the COPY command below more readable and maintainable.
ARG JAR_FILE=/app/target/*.jar

# Copy the executable JAR file from the 'builder' stage into the final image.
COPY --from=builder ${JAR_FILE} application.jar

# Expose the port that the Spring Boot application will run on.
# 8080 is the default for Spring Boot. Render will map this to an external port.
EXPOSE 8080

# The command to run the application when the container starts.
# 'exec' is used to ensure the Java process becomes the container's main process (PID 1),
# which allows it to receive signals like SIGTERM for graceful shutdowns.
ENTRYPOINT ["exec", "java", "-jar", "application.jar"]

