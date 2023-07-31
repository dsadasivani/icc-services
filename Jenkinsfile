pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch:'spring-security-jwt', url: 'https://github.com/dsadasivani/icc-services.git'
            }
        }
        stage('Build') {
            steps {
                // deleteDir()
                sh 'mvn -B package -Dmaven.resources.plugin.version=3.3.1 -Dmaven.compiler.plugin.version=3.11.0 -DskipTests --file pom.xml' // Display Maven Wrapper version and Maven version
                // sh './mvnw clean package -DskipTests'    
            }
        }
        stage('Deploy') {
            steps {
                sh 'cp target/icc-services-1.0-SNAPSHOT.jar ${WORKSPACE}/jars/icc-service'
                sh 'java -jar -Dspring.profiles.active=dev ${WORKSPACE}/jars/icc-service/icc-services-1.0-SNAPSHOT.jar'
            }
        }
    }
}
