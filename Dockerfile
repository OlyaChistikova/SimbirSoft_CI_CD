FROM maven:3.8.5-openjdk-17
WORKDIR /usr/src/app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

CMD ["mvn", "test"]