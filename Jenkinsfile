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
                    junit 'target/surefire-reports/*.xml'
		                echo "Publishing JUnit reports"
            }
        }
         stage('Jacoco Reports') {
             steps {
                 jacoco()
                 echo "Generating Jacoco Code Coverage Reports";
             }
             post {
                 always {
                     // Archive Jacoco code coverage reports
                     archiveArtifacts(artifacts: '**/target/site/jacoco/*', allowEmptyArchive: true)

                     // Publish Jacoco code coverage reports
                     publishHTML([
                         allowMissing: true,
                         alwaysLinkToLastBuild: true,
                         keepAll: true,
                         reportDir: 'target/site/jacoco/',
                         reportFiles: 'index.html',
                         reportName: 'Code Coverage Report',
                     ])
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
    }
 }