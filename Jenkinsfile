pipeline {

    agent any

    stages {

        stage('Checkout Backend Repo') {
            steps {
              git branch: 'main',
              url: 'https://github.com/aminemtar/DEVOPS.git'
            }
        }
        stage('Unit Tests') {
            steps {
                script {

                    sh 'mvn test'
                }
            }
        }
        stage('build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sql') {
                    sh '/opt/homebrew/bin/sonar-scanner'
                }
            }
        }
    }
 }