# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY . .
RUN mvn -DskipTests clean package

# Etapa de runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENV PORT=8080

# Variables de entorno obligatorias (opcionalmente las puedes inyectar en Railway)
ENV RESEND_API_KEY=""
ENV MAIL_FROM=""
ENV DB_URL=""
ENV DB_USERNAME=""
ENV DB_PASSWORD=""

CMD ["java", "-jar", "app.jar"]
