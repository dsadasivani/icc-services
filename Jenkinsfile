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
                // sh 'cp target/icc-services-1.0-SNAPSHOT.jar ${WORKSPACE}/jars/icc-service'
                // sh 'java -jar -Dspring.profiles.active=dev target/icc-services-1.0-SNAPSHOT.jar'
                
                // sh 'screen -dmS my_screen_session bash -c "java -jar -Dspring.profiles.active=dev target/icc-services-1.0-SNAPSHOT.jar"'
                // sh 'screen -ls'
                // sh 'nohup java -jar -Dspring.profiles.active=dev target/icc-services-1.0-SNAPSHOT.jar > app.log 2>&1 &'
                // script {
                //     def appProcess = "nohup java -jar -Dspring.profiles.active=dev target/icc-services-1.0-SNAPSHOT.jar".execute()
                //     appProcess.waitFor()
                // }
                sh 'sudo docker build -t my-app .'
                sh 'sudo docker run -d -p 5000:5000 my-app'
            }
        }
    }
}
