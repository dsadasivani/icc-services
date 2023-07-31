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
                sh 'mvn -B package -DskipTests --file pom.xml' // Display Maven Wrapper version and Maven version
                // sh './mvnw clean package -DskipTests'    
            }
        }
        stage('Create Folder') {
            steps {
                script {
                    def currentDate = new Date().format("yyyy-MM-dd_HH-mm-ss")
                    def folderName = "folder_${currentDate}"
                    sh "mkdir ${folderName}"
                    env.FOLDER_NAME = folderName
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    def folderName = env.FOLDER_NAME
                    sh "echo Using folder: ${folderName}"
                    sh "cp target/icc-services-1.0-SNAPSHOT.jar ./${folderName}/"
                }
                
                // sh 'java -jar -Dspring.profiles.active=dev target/icc-services-1.0-SNAPSHOT.jar'
                
                // sh 'screen -dmS my_screen_session bash -c "java -jar -Dspring.profiles.active=dev target/icc-services-1.0-SNAPSHOT.jar"'
                // sh 'screen -ls'
                // sh 'nohup java -jar -Dspring.profiles.active=dev target/icc-services-1.0-SNAPSHOT.jar > app.log 2>&1 &'
                // script {
                //     def appProcess = "nohup java -jar -Dspring.profiles.active=dev target/icc-services-1.0-SNAPSHOT.jar".execute()
                //     appProcess.waitFor()
                // // }
                // sh 'sudo docker build -t my-app .'
                // sh 'sudo docker run -d -p 5000:5000 my-app'
            }
        }
    }
}
