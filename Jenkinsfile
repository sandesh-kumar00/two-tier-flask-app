pipeline{
    agent{label "vinod"}
    environment{
        SONARHOME = tool "my-sonar"
    
    }
    stages{
        stage("code"){
            steps{
                git url: "https://github.com/sandesh-kumar00/two-tier-flask-app.git/",branch:"main"
            }
        }
        stage("sonar-analysis"){
            steps{
                withSonarQubeEnv("my-sonar"){
                    sh "${env.SONARHOME}/bin/sonar-scanner -Dsonar.projectName=my-app -Dsonar.projectKey=my-app -Dsonar.sources=."
                }
            }
        }
        stage("trivy-FS-scan"){
            steps{
               sh "trivy fs  --exit-code 1 --severity HIGH,CRITICAL --format table -o trivy-report.txt ."
            }
        }
        stage("build the code"){
            steps{
                sh "docker build -t my-app:latest ."
            }
        }
        stage("trivy os-package scan"){
            steps{
              sh "trivy image --exit-code 1 --severity HIGH,CRITICAL --format table -o trivy-image-report.txt my-app:latest"
            }
        }
        stage("push to DockerHub"){
            steps{
                withCredentials([usernamePassword(credentialsId: "docker-cred", usernameVariable: "DOCKER_USER", passwordVariable: "DOCKER_PASS")]){
                 sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin"
                 sh "docker image tag my-app:latest ${DOCKER_USER}/my-app:latest"
                 sh "docker push  ${DOCKER_USER}/my-app:latest"
                }
            }
        }
        stage("deploy on kind-cluster on gcp vm"){
            steps{
                sh "kubectl apply -f ./k8s"
            }
        }
    }
}
