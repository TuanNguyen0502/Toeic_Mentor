# 1. Build stage (JDK + Maven)
FROM maven:3.9.11-eclipse-temurin-24-noble AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. Runtime stage (JRE only)
FROM eclipse-temurin:24.0.2_12-jre-ubi9-minimal
WORKDIR /app

# Install MySQL client + openssl (để lấy cert)
RUN microdnf install -y mysql openssl && microdnf clean all

# Get SSL certificate from generativelanguage.googleapis.com and import into keystore
RUN echo | openssl s_client -showcerts -connect generativelanguage.googleapis.com:443 2>/dev/null \
    | openssl x509 -outform PEM > /tmp/googleapis.crt \
    && keytool -import -trustcacerts -alias googleapis \
       -file /tmp/googleapis.crt \
       -keystore ${JAVA_HOME}/lib/security/cacerts \
       -storepass changeit -noprompt \
    && rm /tmp/googleapis.crt

# Copy app JAR and wait script
COPY --from=build /app/target/*.jar app.jar
COPY wait-for-mysql.sh /app/wait-for-mysql.sh
RUN chmod +x /app/wait-for-mysql.sh

# Run app after MySQL is ready
ENTRYPOINT ["sh", "-c", "/app/wait-for-mysql.sh && java -jar app.jar"]
