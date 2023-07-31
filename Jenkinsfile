pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/dsadasivani/icc-services.git'
            }
        }
        stage('Build') {
            steps {
                dir('icc-services') {
                    deleteDir()
                    sh './mvnw --version' // Display Maven Wrapper version and Maven version
                    sh './mvnw clean package -DskipTests'
                }     
            }
        }
        stage('Deploy') {
            steps {
                sh 'cp target/icc-services-1.0-SNAPSHOT.jar /home/crazy7/icc-artifacts/dev/icc-service'
                sh 'java -jar -Dspring.profiles.active=dev /home/crazy7/icc-artifacts/dev/icc-service/icc-services-1.0-SNAPSHOT.jar'
            }
        }
    }
}
