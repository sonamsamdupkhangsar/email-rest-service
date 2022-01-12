FROM maven:3-openjdk-17-slim as build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN ["mvn", "clean", "install"]

FROM openjdk:16
WORKDIR /app
COPY --from=build /app/target/email-rest-service-1.0-SNAPSHOT.jar /app/email-rest-service.jar
EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/app/email-rest-service.jar"]

