# ================================
# Stage 1: Build the application
# ================================

FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven configuration
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy application source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests


# ================================
# Stage 2: Run the application
# ================================

FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy generated JAR
COPY --from=build /app/target/*.jar app.jar

# Application port
EXPOSE 8080

# Start application
ENTRYPOINT ["java", "-jar", "app.jar"]