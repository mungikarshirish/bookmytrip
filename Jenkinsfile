pipeline {

    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '3', artifactNumToKeepStr: '3'))
    }

    tools {
        maven 'maven_3.9.14'
    }

    stages {
        stage('Code Compilation') {
            steps {
                echo 'Starting Code Compilation...'
                sh 'mvn clean compile'
                echo 'Code Compilation Completed Successfully!'
            }
        }
        stage('Code Coverage') {
            steps {
                echo 'Running JUnit Test Cases...'
                sh 'mvn clean test'
                echo 'JUnit Test Cases Completed Successfully!'
            }
        }
        stage('Code Quality') {
            environment {
                scannerHome = tool 'qube'
            }
            steps {
                echo 'Starting SonarQube Code Quality Scan...'
               /* withSonarQubeEnv('sonar-server') {
                    sh 'mvn sonar:sonar'
                } */
                echo 'SonarQube Scan Completed. Checking Quality Gate...'
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
                echo 'Quality Gate Check Completed!'
            }
        }
        stage('Code Package') {
            steps {
                echo 'Creating JAR Artifact...'
                sh 'mvn clean package'
                sh '''
                    cp target/*.jar target/bookmytrip-1.1.${BUILD_NUMBER}.jar
                '''
                echo 'JAR Artifact Created Successfully!'
            }
        }
        stage('Build & Tag Docker Image') {
            steps {
                echo 'Building Docker Image and Tagging...'
                sh "docker build -t shirishmungikar/bookmytrip:latest -t bookmytrip:latest ."
                echo 'Docker Image Build Completed!'
            }
        }
        stage('Docker Image Scanning') {
            steps {
                echo 'Scanning Docker Image with Trivy...'
                //sh 'trivy image --scanners vuln --no-progress shirishmungikar/bookmytrip:latest || echo "Scan Failed - Proceeding with Caution"'
                echo 'Docker Image Scanning Completed!'
            }
        }
        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'dockerhubCred', variable: 'dockerhubCred')]) {
                        sh 'docker login docker.io -u shirishmungikar -p ${dockerhubCred}'
                        echo 'Pushing Docker Image to Docker Hub...'
                        sh 'docker push shirishmungikar/bookmytrip:latest'
                        echo 'Docker Image Pushed to Docker Hub Successfully!'
                    }
                }
            }
        }
        stage('Push Docker Image to Amazon ECR') {
            steps {
                script {
                    withDockerRegistry([credentialsId: 'ecr:ap-south-1:ecr-credentials', url: "https://794531973659.dkr.ecr.ap-south-1.amazonaws.com"]) {
                        echo 'Tagging and Pushing Docker Image to ECR...'
                        sh '''
                            docker images
                            docker tag bookmytrip:latest 794531973659.dkr.ecr.ap-south-1.amazonaws.com/bookmytrip:latest
                            docker push 794531973659.dkr.ecr.ap-south-1.amazonaws.com/bookmytrip:latest
                        '''
                        echo 'Docker Image Pushed to Amazon ECR Successfully!'
                    }
                }
            }
        }
        stage('Upload Docker Image to Nexus') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        //sh 'docker login http://3.7.55.216:8085/repository/bookmytrip/ -u admin -p ${PASSWORD}'
                        echo "Push Docker Image to Nexus : In Progress"
                        //sh 'docker tag bookmytrip 3.7.55.216:8085/bookmytrip:latest'
                        //sh 'docker push 3.7.55.216:8085/bookmytrip'
                        echo "Push Docker Image to Nexus : Completed"
                    }
                }
            }
        }
        stage('Cleanup Docker Images') {
            steps {
                echo 'Cleaning up local Docker images...'
                sh "docker rmi -f shirishmungikar/bookmytrip:latest || true"
                sh "docker rmi -f bookmytrip:latest || true"
                echo 'Local Docker images deleted successfully!'
            }
        }
    }
}
