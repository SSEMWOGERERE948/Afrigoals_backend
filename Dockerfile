# ---------- Build Stage ----------
FROM maven:3.8-openjdk-17-slim AS build
WORKDIR /app

# Copy project files from the user-module subdirectory
COPY user-module/pom.xml .
COPY user-module/src ./src

# Build the application (skip tests to speed up build)
RUN mvn clean package -DskipTests

# ---------- Runtime Stage ----------
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built artifact from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the app port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
