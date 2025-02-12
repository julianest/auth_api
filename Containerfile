FROM gradle:jdk17-corretto-al2023 AS build

WORKDIR /app

COPY build.gradle settings.gradle /app/

COPY src /app/src

RUN gradle clean
RUN gradle build

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/auth_api-1.0.0.jar /app/AuthAPI.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "AuthAPI.jar"]