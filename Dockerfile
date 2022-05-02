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

ENTRYPOINT java -Dserver.host=$HOST -Dserver.port=$PORT -Dspring.data.mongodb.port=$MONGO_PORT -Dspring.data.mongodb.host=$MONGO_HOST -Dspring.data.mongodb.database=$MONGO_DB -Dspring.data.mongodb.username=$MONGO_USER -Dspring.data.mongodb.password=$MONGO_PASS -Dsecurity.jwt.token.secret-key=$JWT_SECRET -Dsecurity.jwt.token.expire-length=$JWT_EXPIRATION -Dsecurity.api_key.valid=$API_KEY -Dsecurity.frontend_app.url_pattern=$CORS_FRONT_URL -Dcode_executor_service.host=$EXEC_SERVICE_HOST  -Dcode_executor_service.port=$EXEC_SERVICE_PORT -Dode_executor_service.api_key=$EXEC_SERVICE_API_KEY -Dcode_executor_service.execute_route=$EXEC_SERVICE_EXEC_ROUTE -Dcode_executor_service.alive_route=$EXEC_SERVICE_ALIVE_ROUTE -jar $ARTIFACT_NAME