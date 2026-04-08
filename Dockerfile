FROM gradle:9.0.0-jdk25 AS build
WORKDIR /workspace
COPY --chown=gradle:gradle . .
RUN gradle :app:bootJar --no-daemon -x test

FROM eclipse-temurin:25-jre
WORKDIR /app
RUN groupadd -r app && useradd -r -g app app
COPY --from=build /workspace/app/build/libs/*.jar app.jar
USER app
EXPOSE 8080
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-jar","/app/app.jar"]
