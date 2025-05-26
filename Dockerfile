FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/EVMitTestTask.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]