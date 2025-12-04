# ===== Stage 1: Build the app =====
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy Maven config + source
COPY pom.xml .
COPY src ./src

# Build the JAR (skip tests to speed it up)
RUN mvn -q clean package -DskipTests

# ===== Stage 2: Run the app =====
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/toto-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot's default port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
