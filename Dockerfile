FROM gradle:7.4.0-jdk17-alpine AS TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle $APP_HOME

COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . /home/gradle/src

USER root

RUN chown -R gradle /home/gradle/src

RUN gradle bootJar || return 0

COPY . .

RUN gradle clean build

FROM openjdk:17.0.2-jdk
ENV ARTIFACT_NAME=rsreu-contests-system-backend-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app

WORKDIR $APP_HOME

COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .

ENTRYPOINT java -Dspring.data.mongodb.port=$MONGO_PORT -Dspring.data.mongodb.host=$MONGO_HOST -jar $ARTIFACT_NAME