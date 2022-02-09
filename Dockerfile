FROM gradle:7.3.3-jdk8 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

# We are compiling the whole app so you must use an adaptated JRE
FROM openjdk:16-alpine3.13
COPY --from=build /home/gradle/src/build/libs/*.jar /kotlin/app.jar

ENTRYPOINT ["java","-jar","/kotlin/app.jar"]
