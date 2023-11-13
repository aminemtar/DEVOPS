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
         stage('build') {
                    steps {
                        sh 'mvn package'
                    }
                    post {
                      success {
                      emailext subject: 'Jenkins build Success',
                      body: 'The Jenkins build  has succeeded. Build URL: ${BUILD_URL}',
                       to: '$DEFAULT_RECIPIENTS'
                                }

                       failure {
                       emailext subject: 'Jenkins build Failure',
                       body: 'The Jenkins build has failed. Build URL: ${BUILD_URL}',
                       to: '$DEFAULT_RECIPIENTS'
                                 }
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
                                    git branch: 'main',
                                    url: 'https://github.com/aminemtar/DEVOPS-FRONTEND.git'
                                    sh 'npm install -g @angular/cli'
                                    sh 'npm install'
                                    sh 'ng build --configuration=production'

                                    script{
                                     sh 'docker build -t mtar/angular-app -f Dockerfile .'
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
                      sh 'docker compose up --build -d'
                             }
                           }
                          }


    }
    post {
     success {
      emailext subject: 'Jenkins build Success',
      body: 'The Jenkins build  has succeeded. Build URL: ${BUILD_URL}',
      to: '$DEFAULT_RECIPIENTS'
       }

       failure {
       emailext subject: 'Jenkins build Failure',
       body: 'The Jenkins build has failed. Build URL: ${BUILD_URL}',
        to: '$DEFAULT_RECIPIENTS'
                            }
                        }
 }