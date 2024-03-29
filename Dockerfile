FROM gradle:8.2.1-jdk17-alpine AS build

ARG USER
ARG PASSWORD

ENV GITHUB_USERNAME=$USER
ENV GITHUB_TOKEN=$PASSWORD

RUN mkdir -p /workspace
WORKDIR /workspace
COPY . /workspace
RUN chmod +x gradlew
RUN ./gradlew --no-daemon build -x test

FROM eclipse-temurin:17-alpine

ENV TARGET_ENV=dev
ENV CONFIG_LOCATION=/etc/config/application.properties

COPY api/src/main/resources/application.properties /etc/config/application.properties
COPY --from=build /workspace/api/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-DenvTarget=${TARGET_ENV}", "-jar","/app.jar", "--spring.config.location=${CONFIG_LOCATION}"]