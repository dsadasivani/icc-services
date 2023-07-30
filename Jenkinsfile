pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from GitHub or your version control system
                // If the repository is local, you can use 'file://' instead of the remote URL
                git 'https://github.com/dsadasivani/icc-services.git'
            }
        }
        stage('Build') {
            steps {
                // Build your Spring Boot application using Maven
                bat 'mvn clean package spring-boot:repackage'
            }
        }
        stage('Deploy') {
            steps {
                // Deploy the application to your local Windows system
                // For example, copying the JAR to a directory
                bat 'xcopy /Y /Q target\\icc-services-1.0-SNAPSHOT.jar C:\\Dilip\\deployment-artifacts\\icc-services'
                bat 'java -jar -Dspring.profiles.active=local C:\\Dilip\\deployment-artifacts\\icc-services\\icc-services-1.0-SNAPSHOT.jar'
                // Or bat 'xcopy /Y /Q target\\your-spring-boot-app.jar C:\\path\\to\\deploy' // If you want to overwrite existing files
            }
        }
    }
}

