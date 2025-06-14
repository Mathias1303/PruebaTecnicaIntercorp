FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/*.jar app.jar

RUN mkdir -p /customer-data/input \
    && mkdir -p /customer-data/processed \
    && mkdir -p /customer-data/error

ENV SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
ENV APP_XML_INPUT_DIR=/customer-data/input
ENV APP_XML_PROCESSED_DIR=/customer-data/processed
ENV APP_XML_ERROR_DIR=/customer-data/error

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]