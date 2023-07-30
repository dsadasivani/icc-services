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
                sh 'java --version'
                // Build your Spring Boot application using Maven
                sh 'mvn -B package -Dmaven.resources.plugin.version=3.3.1 -Dmaven.compiler.plugin.version=3.11.0 -DskipTests --file pom.xml'
            }
        }
        stage('Deploy') {
            steps {
                // Deploy the application to your local Windows system
                // For example, copying the JAR to a directory
                sh 'cp target/icc-services-1.0-SNAPSHOT.jar /home/crazy7/icc-artifacts/dev/icc-service'
                sh 'java -jar -Dspring.profiles.active=dev /home/crazy7/icc-artifacts/dev/icc-service/icc-services-1.0-SNAPSHOT.jar'
                // Or bat 'xcopy /Y /Q target\\your-spring-boot-app.jar C:\\path\\to\\deploy' // If you want to overwrite existing files
            }
        }
    }
}

