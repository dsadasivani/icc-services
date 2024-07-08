# Stage 1: Build the application
FROM maven:3.9.8-eclipse-temurin-21-alpine AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml /app
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src /app/src
RUN mvn clean package -DskipTests

# Use an OpenJDK 21 base image
FROM openjdk:21

# Set the working directory inside the container
WORKDIR /app

# Copy the executable JAR file into the container
COPY --from=build /app/target/icc-services-1.0-SNAPSHOT.jar /app/app.jar

# Expose the port that your Spring Boot application runs on
EXPOSE 5000

# Set the command to run your application
CMD ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILE}", "app.jar"]
