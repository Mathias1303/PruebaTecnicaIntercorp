# Dockerfile para microservice-04

# Usamos la misma imagen base de Java 17
FROM eclipse-temurin:17-jdk-jammy

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo JAR de la aplicación compilada
COPY target/*.jar app.jar

# Exponemos el puerto 8088, que es el que usará el front-end para comunicarse
EXPOSE 8088

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]