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
         stage('JUNit Reports') {
                    steps {
                            junit 'target/surefire-reports/*.xml'
        		                echo "Publishing JUnit reports"
                    }
                }
                stage('Jacoco Reports') {
                            steps {
                                  jacoco()
                                  echo "Publishing Jacoco Code Coverage Reports";
                            }
                        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(installationName:'sql') {
                sh 'chmod +x ./mvnw'
                    sh 'mvn package sonar:sonar'
                }
            }
        }
    }
 }