pipeline {

    agent any
tools { nodejs '19.9.0'}
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
            post{
            always{
            junit '**/target/surefire-reports/TEST-*.xml'
            }
            }
        }
        stage('build') {
            steps {
                sh 'mvn package'
            }
        }

stage('JUNit Reports') {
            steps {
                    junit '**/target/surefire-reports/*.xml'
		                echo "Publishing JUnit reports"
            }
        }
         stage('Jacoco Reports') {
                    steps {

                          echo "Publishing Jacoco Code Coverage Reports";
                    }
                     post {
                                                 success {
                                                     jacoco(
                                                         execPattern: '**/target/*.exec',
                                                     )
                                                 }
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
        stage('Build docker image'){
                                steps{
                                    script{

                                        sh 'docker build -t mtar/devops-project .'
                                    }
                                }
                            }

         stage('Push image to Hub'){
              steps{
                   script{
                           withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                              sh 'docker login -u mtar -p ${dockerhubpwd}'
                              sh 'docker push mtar/devops-project'
                           }
                           }
                   }
                   }
        stage('Build Frontend') {
        agent any
            steps {
                git branch: 'main',
                           url: 'https://github.com/aminemtar/DEVOPS-FRONTEND.git'
                           sh 'npm install -g @angular/cli'
                           sh 'npm install'
                           sh 'ng build --configuration=production'
                            // Build and push Docker image for the frontend
                            script{
                             sh 'docker build -t mtar/angular-app -f Dockerfile .'
                              withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                              sh 'docker login -u mtar -p ${dockerhubpwd}'
                              sh 'docker push mtar/angular-app'
                              }
                       }
                    }
                  }
                   stage('docker-compose  backend'){
                                                steps{
                                                 script{
                                                   sh 'docker compose up -d'
                                                    }
                                                      }
                                                      }

    }
 }