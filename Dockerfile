FROM maven:3.9.16-eclipse-temurin-21@sha256:99e61abcff91a9b1333463bd8451fb18495d6eba9250ac66a338b518f8278320 AS build

RUN mkdir -p /workspace
WORKDIR /workspace
COPY . /workspace

RUN --mount=type=secret,id=github_token,env=GITHUB_TOKEN --mount=type=secret,id=github_username,env=GITHUB_USERNAME mvn -s settings.xml -B -f pom.xml clean package -DskipTests

FROM eclipse-temurin:21-alpine@sha256:0bfc69a4758a86710e5c474032d28400a8bd00874766f9e8b1642ac2fd293159

ENV TARGET_ENV=dev
ENV CONFIG_LOCATION=/etc/config/application.yaml

COPY --from=build /workspace/api/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-DenvTarget=${TARGET_ENV}", "-jar","/app.jar", "--spring.config.location=${CONFIG_LOCATION}"]