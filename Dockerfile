# Use an OpenJDK 21 base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the executable JAR file into the container
COPY target/icc-services-1.0-SNAPSHOT.jar /app/app.jar

# Expose the port that your Spring Boot application runs on
EXPOSE 5000

# Set the command to run your application
CMD ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILE}", "app.jar"]
