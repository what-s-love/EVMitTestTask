FROM node:18 as frontend-builder

WORKDIR /app
COPY frontend/package*.json ./
RUN npm install

COPY frontend ./frontend
WORKDIR /app/frontend
RUN npm run build

FROM eclipse-temurin:17-jdk-jammy as java-builder

WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=java-builder /app/target/*.jar app.jar
#COPY --from=frontend-builder /app/frontend/dist/tailwind.css /app/src/main/resources/static/css/tailwind.css

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
