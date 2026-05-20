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
                script {
                    if (isUnix()) {
                        sh 'mvn -B -DskipTests clean package'
                    } else {
                        bat 'mvn -B -DskipTests clean package'
                    }
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker build -t ${DOCKERHUB_IMAGE}:${DOCKERHUB_TAG} .'
                    } else {
                        bat 'docker build -t %DOCKERHUB_IMAGE%:%DOCKERHUB_TAG% .'
                    }
                }
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                    script {
                        if (isUnix()) {
                            sh 'echo "$DOCKERHUB_PASSWORD" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin'
                        } else {
                            bat 'echo %DOCKERHUB_PASSWORD% | docker login -u %DOCKERHUB_USERNAME% --password-stdin'
                        }
                    }
                }
            }
        }

        stage('Docker Tag') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker tag ${DOCKERHUB_IMAGE}:${DOCKERHUB_TAG} ${DOCKERHUB_IMAGE}:${DOCKERHUB_TAG}'
                    } else {
                        bat 'docker tag %DOCKERHUB_IMAGE%:%DOCKERHUB_TAG% %DOCKERHUB_IMAGE%:%DOCKERHUB_TAG%'
                    }
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker push ${DOCKERHUB_IMAGE}:${DOCKERHUB_TAG}'
                    } else {
                        bat 'docker push %DOCKERHUB_IMAGE%:%DOCKERHUB_TAG%'
                    }
                }
            }
        }

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
                script {
                    if (isUnix()) {
                        sh 'mvn -B -DskipTests clean package'
                    } else {
                        bat 'mvn -B -DskipTests clean package'
                    }
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker build --no-cache -t ${DOCKERHUB_IMAGE}:${DOCKERHUB_TAG}  .'
                    } else {
                        bat 'docker build -t %DOCKERHUB_IMAGE%:%DOCKERHUB_TAG% .'
                    }
                }
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

                    script {
                        if (isUnix()) {
                            sh 'echo "$DOCKERHUB_PASSWORD" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin'
                        } else {
                            bat 'echo %DOCKERHUB_PASSWORD% | docker login -u %DOCKERHUB_USERNAME% --password-stdin'
                        }
                    }
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker push ${DOCKERHUB_IMAGE}:${DOCKERHUB_TAG}'
                    } else {
                        bat 'docker push %DOCKERHUB_IMAGE%:%DOCKERHUB_TAG%'
                    }
                }
            }
        }

       stage('Deploy Container') {
    steps {
        script {
            if (isUnix()) {

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

            } else {

                bat '''
                    docker stop %CONTAINER_NAME% || echo Container not running
                    docker rm %CONTAINER_NAME% || echo Container not found

                    docker rmi %DOCKERHUB_IMAGE%:%DOCKERHUB_TAG% || echo Image not found

                    docker pull %DOCKERHUB_IMAGE%:%DOCKERHUB_TAG%

                    docker run -d --name %CONTAINER_NAME% -p %DEPLOY_PORT%:%APP_PORT% %DOCKERHUB_IMAGE%:%DOCKERHUB_TAG%
                '''

            }
        }
    }
}
    }
