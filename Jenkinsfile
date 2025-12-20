pipeline {
    agent any

    triggers {
        pollSCM('* * * * *')
    }

    options {
        timeout(time: 15, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                // На Windows используем bat
                bat 'git submodule update --init --recursive'
            }
        }

        stage('Verify & Pull') {
            steps {
                script {
                    bat 'docker --version'
                    bat 'docker-compose --version'
                    echo 'Загрузка образов браузеров...'
                    bat 'docker pull selenoid/vnc:chrome_128.0'
                    bat 'docker pull selenoid/video-recorder:latest-release'
                }
            }
        }

        stage('Build and Test') {
            steps {
                script {
                    echo 'Очистка старых контейнеров...'
                    bat 'docker-compose down -v --remove-orphans'

                    echo 'Запуск инфраструктуры...'
                    // Запускаем всё в фоне, кроме тестов
                    bat 'docker-compose up -d selenoid selenoid-ui'

                    echo 'Ожидание готовности Selenoid (порт 4444)...'
                    // Динамическая проверка порта вместо ping
                    bat 'powershell -Command "for($i=0; $i -lt 30; $i++) { if(Test-NetConnection localhost -Port 4444) { exit 0 }; Start-Sleep -Seconds 1 }; exit 1"'

                    echo 'Запуск тестов...'
                    // Запускаем тесты. Если они упадут, Jenkins пометит стадию как FAILURE
                    bat 'docker-compose up --build --abort-on-container-exit --exit-code-from test-runner test-runner'
                }
            }
        }

        stage('Collect Results') {
            steps {
                script {
                    // Копируем результаты из контейнера, если они не проброшены через volume
                    // bat 'docker cp test_runner:/usr/src/app/target ./target'
                    bat 'dir /s target\\surefire-reports || echo "Reports not found"'
                }
            }
        }
    }

    post {
        always {
            script {
                // Публикация результатов тестов (JUnit)
                junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true

                // Генерация и публикация Allure
                allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
                ])

                // Архивация всех артефактов (логи, скриншоты)
                archiveArtifacts artifacts: 'target/**/*', allowEmptyArchive: true
            }
        }
        cleanup {
            echo 'Остановка всех контейнеров проекта...'
            bat 'docker-compose down -v'
        }
    }
}