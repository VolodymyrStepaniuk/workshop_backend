# syntax=docker/dockerfile:experimental
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace/all

COPY ./gradle /workspace/all/gradle
COPY ./gradlew /workspace/all/gradlew
COPY ./settings.gradle /workspace/all/settings.gradle
COPY ./projects /workspace/all/projects
COPY ./libs /workspace/all/libs
COPY ./lombok.config /workspace/all/lombok.config
RUN --mount=type=cache,target=/root/.gradle ./gradlew :telegram-bot:clean :telegram-bot:build -x :telegram-bot:test

WORKDIR /workspace/all/projects/telegram-bot
RUN mkdir -p target/extracted
RUN java -Djarmode=layertools -jar build/libs/*-all.jar extract --destination target/extracted

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG APP=/workspace/all/projects/telegram-bot
ARG EXTRACTED=${APP}/target/extracted
COPY --from=build ${APP}/start-app.sh ./
COPY --from=build ${EXTRACTED}/dependencies/ ./
COPY --from=build ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=build ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=build ${EXTRACTED}/application/ ./
RUN chmod +x ./start-app.sh
ENTRYPOINT ["./start-app.sh"]