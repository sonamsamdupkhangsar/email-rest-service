FROM maven:3-openjdk-17-slim as build

WORKDIR /app

COPY pom.xml settings.xml ./
COPY src ./src

RUN --mount=type=secret,id=PERSONAL_ACCESS_TOKEN \
   export PERSONAL_ACCESS_TOKEN=$(cat /run/secrets/PERSONAL_ACCESS_TOKEN) && \
   mvn -s settings.xml clean install

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/email-rest-service-1.3-SNAPSHOT.jar /app/email-rest-service.jar
EXPOSE 8080

LABEL org.opencontainers.image.source https://github.com/sonamsamdupkhangsar/email-rest-service

ENTRYPOINT [ "java", "-jar", "/app/email-rest-service.jar"]

