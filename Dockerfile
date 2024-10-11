# Etapa 1: Usando Maven para construir a aplicação
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Usando JDK para rodar o jar da aplicação
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expondo a porta que a aplicação Spring Boot usa
EXPOSE 8080

# Comando de inicialização do Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
