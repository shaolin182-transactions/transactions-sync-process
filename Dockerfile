FROM maven:3.9.16-eclipse-temurin-21 AS build

RUN mkdir -p /workspace
WORKDIR /workspace
COPY . /workspace

RUN --mount=type=secret,id=github_token,env=GITHUB_TOKEN --mount=type=secret,id=github_username,env=GITHUB_USERNAME mvn -s settings.xml -B -f pom.xml clean package -DskipTests

FROM eclipse-temurin:21-alpine

ENV TARGET_ENV=dev
ENV CONFIG_LOCATION=/etc/config/application.yaml

COPY --from=build /workspace/api/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-DenvTarget=${TARGET_ENV}", "-jar","/app.jar", "--spring.config.location=${CONFIG_LOCATION}"]