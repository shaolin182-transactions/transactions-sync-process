FROM maven:3.9.16-eclipse-temurin-21 AS build

ARG USER
ARG PASSWORD

ENV GITHUB_USERNAME=$USER
ENV GITHUB_TOKEN=$PASSWORD

RUN mkdir -p /workspace
WORKDIR /workspace
COPY . /workspace

RUN mvn -B -f pom.xml clean package -DskipTests

FROM eclipse-temurin:21-alpine

ENV TARGET_ENV=dev
ENV CONFIG_LOCATION=/etc/config/application.yaml

COPY --from=build /workspace/api/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-DenvTarget=${TARGET_ENV}", "-jar","/app.jar", "--spring.config.location=${CONFIG_LOCATION}"]