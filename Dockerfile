# Use an official OpenJDK 17 runtime as a base image
FROM openjdk:17-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the executable JAR file into the container
COPY target/icc-services-1.0-SNAPSHOT.jar /app/app.jar

# Set the command to run your application
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "app.jar"]
