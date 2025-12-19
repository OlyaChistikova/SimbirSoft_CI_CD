# Используем официальный образ OpenJDK с Maven
FROM maven:3.8.5-openjdk-17

# Создаем рабочую папку
WORKDIR /app

# Копируем файлы проекта в контейнер
COPY . /app

RUN chmod +x wait-for-it.sh

# Устанавливаем переменные окружения
ENV MAVEN_OPTS="-Xmx2g"

# Команда по умолчанию — запуск тестов
CMD ["mvn", "clean", "test"]
