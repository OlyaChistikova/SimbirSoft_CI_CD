pipeline {
    agent any

    triggers {
        pollSCM('* * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                bat 'git submodule update --init --recursive'
            }
        }

        stage('Verify Tools') {
            steps {
                bat 'docker --version'
                bat 'docker-compose --version'
            }
        }

        stage('Pull Dependencies') {
            steps {
                script {
                    echo 'Скачивание образов браузеров для Selenoid...'
                    bat 'docker pull selenoid/chrome:latest || echo "Failed to pull chrome"'
                    bat 'docker pull selenoid/video-recorder:latest-release || echo "Failed to pull video-recorder"'
                }
            }
        }

        stage('Build and Test') {
            steps {
                script {
                    bat 'docker rm -f test_runner || echo "No container to remove"'
                    bat 'docker-compose down -v --remove-orphans || echo "No containers to stop"'
                    bat 'ping -n 11 127.0.0.1 > nul'
                    bat 'docker-compose up --build --abort-on-container-exit --exit-code-from test-runner test-runner'
                }
            }
        }

        stage('Collect Reports') {
            steps {
                script {
                    bat 'dir /s target || echo "No target directory"'

                    bat 'dir /s target\\surefire-reports 2>nul || echo "No surefire-reports directory"'
                }
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true

                    archiveArtifacts artifacts: 'target/**/*', allowEmptyArchive: true
                }
            }

        }
        stage('Generate Allure Report') {
                    steps {
                        bat 'mvn allure:install'
                        bat 'mvn allure:report'
                    }
                }
        stage('Publish Allure Report') {
            steps {
                allure([
                    results: ['target/allure-results'],
                    report: 'target/allure-report'
                ])
            }
        }
    }
}
