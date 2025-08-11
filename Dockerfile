# 1. Dùng JDK để build (stage 1)
FROM maven:3.9.11-eclipse-temurin-24-noble AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. Dùng JRE để chạy (stage 2)
FROM eclipse-temurin:24.0.2_12-jre-ubi9-minimal
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# 3. Command để chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]
