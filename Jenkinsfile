pipeline {
    agent any

    environment {
        DOCKERHUB_IMAGE = "yashwanthmk/springboot-app"
        DOCKERHUB_TAG = "latest"
        CONTAINER_NAME = "springboot-app"
        DEPLOY_PORT = "8085"
        APP_PORT = "8080"
    }

    options {
        timestamps()
    }

    stages {

        stage('Clone Repository') {
            steps {
                checkout scm
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build --no-cache -t ${DOCKERHUB_IMAGE}:${DOCKERHUB_TAG} .'
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKERHUB_USERNAME',
                        passwordVariable: 'DOCKERHUB_PASSWORD'
                    )
                ]) {

                    sh 'echo "$DOCKERHUB_PASSWORD" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin'
                }
            }
        }

        stage('Docker Push') {
            steps {
                sh 'docker push ${DOCKERHUB_IMAGE}:${DOCKERHUB_TAG}'
            }
        }

        stage('Deploy Container') {
            steps {

                sh '''
                    docker stop ${CONTAINER_NAME} || true
                    docker rm ${CONTAINER_NAME} || true

                    docker rmi ${DOCKERHUB_IMAGE}:${DOCKERHUB_TAG} || true

                    docker pull ${DOCKERHUB_IMAGE}:${DOCKERHUB_TAG}

                    docker run -d \
                    --name ${CONTAINER_NAME} \
                    -p ${DEPLOY_PORT}:${APP_PORT} \
                    ${DOCKERHUB_IMAGE}:${DOCKERHUB_TAG}
                '''
            }
        }
    }

    post {
        always {
            sh 'docker logout || true'
        }
    }
}
