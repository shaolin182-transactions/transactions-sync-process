FROM maven:3.9.16-eclipse-temurin-21@sha256:a972570be789ee5c9fa23446a8914ac7327560b5c022f662cfa9452aef829f18 AS build

RUN mkdir -p /workspace
WORKDIR /workspace
COPY . /workspace

RUN --mount=type=secret,id=github_token,env=GITHUB_TOKEN --mount=type=secret,id=github_username,env=GITHUB_USERNAME mvn -s settings.xml -B -f pom.xml clean package -DskipTests

FROM eclipse-temurin:21-alpine@sha256:6ea5548706b60ac0a602eaf48af74792cbab012d90e811ca8db6184b16b5c3d6

ENV TARGET_ENV=dev
ENV CONFIG_LOCATION=/etc/config/application.yaml

COPY --from=build /workspace/api/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-DenvTarget=${TARGET_ENV}", "-jar","/app.jar", "--spring.config.location=${CONFIG_LOCATION}"]