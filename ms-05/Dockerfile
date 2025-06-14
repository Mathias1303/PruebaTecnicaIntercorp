# Usamos la imagen base de Java 17
FROM eclipse-temurin:17-jdk-jammy

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo JAR de la aplicación
COPY target/*.jar app.jar

# Crear los directorios necesarios para los XML
RUN mkdir -p /customer-data/input \
    && mkdir -p /customer-data/processed \
    && mkdir -p /customer-data/error

# Variables de entorno para la configuración
ENV SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
ENV APP_XML_INPUT_DIR=/customer-data/input
ENV APP_XML_PROCESSED_DIR=/customer-data/processed
ENV APP_XML_ERROR_DIR=/customer-data/error

# Puerto expuesto (ajusta según tu aplicación)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]