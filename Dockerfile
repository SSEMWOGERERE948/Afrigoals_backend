# ---------- Build Stage ----------
FROM maven:3.8-openjdk-17-slim AS build
WORKDIR /app

# Copy project files (since src and pom.xml are in repo root)
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# ---------- Runtime Stage ----------
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built artifact from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port Render (or any host) should use
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
