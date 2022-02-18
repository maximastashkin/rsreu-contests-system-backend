FROM openjdk:17.0.2-jdk

ADD build/libs/rsreu-contests-system-backend-0.0.1-SNAPSHOT.jar /usr/src/service.jar

WORKDIR /usr/src

ENTRYPOINT java -Dspring.data.mongodb.port=$MONGO_PORT -Dspring.data.mongodb.host=$MONGO_HOST -jar service.jar