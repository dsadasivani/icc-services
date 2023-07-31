pipeline {
    agent any

    parameters {
        // Add a parameter of type "Branch"
        gitParameter name: 'BRANCH', type: 'PT_BRANCH', defaultValue: 'master', branchFilter: '.*', sortMode: 'DESCENDING'
    }
    
    stages {
        stage('Checkout') {
            steps {
                // git branch:${params.BRANCH}, url: 'https://github.com/dsadasivani/icc-services.git'
                checkout([$class: 'GitSCM',
                          branches: [[name: "${params.BRANCH}"]],
                          doGenerateSubmoduleConfigurations: false,
                          extensions: [],
                          submoduleCfg: [],
                          userRemoteConfigs: [[url: 'https://github.com/dsadasivani/icc-services.git']]])
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -B package -DskipTests --file pom.xml'
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
            }
        }
    }
}
