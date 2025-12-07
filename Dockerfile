# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -B -DskipTests clean package \
    && cp target/*.jar app.jar

# Etapa de runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/app.jar app.jar

EXPOSE 8080
ENV PORT=8080

CMD ["java", "-Dlogging.level.root=DEBUG", "-jar", "app.jar"]
