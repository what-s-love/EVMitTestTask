# Этап сборки фронтенда
FROM node:18 as frontend-builder

WORKDIR /app/frontend
COPY src/main/frontend/package.json frontend/package-lock.json ./
RUN npm install

COPY src/main/frontend .
RUN npm run build

FROM eclipse-temurin:17-jdk-jammy as java-builder

WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=java-builder /app/target/*.jar app.jar
COPY --from=frontend-builder /app/frontend/src/main/resources/static/css/tailwind.css /app/resources/static/css/tailwind.css

ENTRYPOINT ["java", "-jar", "app.jar"]