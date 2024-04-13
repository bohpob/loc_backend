# Build stage
FROM gradle:8.5.0-jdk11-alpine AS build

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle buildFatJar --no-daemon

# Package stage
FROM adoptopenjdk/openjdk11:alpine

EXPOSE 8080:8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/ktor-docker.jar

ENTRYPOINT ["java","-jar","/app/ktor-docker.jar"]