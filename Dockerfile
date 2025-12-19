# Используем официальный образ OpenJDK с Maven
FROM maven:3.8.5-openjdk-17

WORKDIR /usr/src/app

# Копирование pom.xml
COPY pom.xml .

# Установка зависимостей
RUN mvn dependency:go-offline -B

ENV ALLURE_VERSION=2.22.0

RUN mkdir -p /opt/allure \
  && curl -L -o /tmp/allure.zip https://github.com/allure-framework/allure2/releases/download/${ALLURE_VERSION}/allure-${ALLURE_VERSION}.zip \
  && unzip /tmp/allure.zip -d /opt/allure \
  && ln -s /opt/allure/allure-${ALLURE_VERSION}/bin/allure /usr/local/bin/allure \
  && rm /tmp/allure.zip

# Копирование исходного кода
COPY src ./src

# Запуск тестов напрямую
CMD ["mvn", "test"]
