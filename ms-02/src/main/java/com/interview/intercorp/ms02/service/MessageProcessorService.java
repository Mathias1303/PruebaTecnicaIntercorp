package com.interview.intercorp.ms02.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class MessageProcessorService {

    private static final Logger logger = LoggerFactory.getLogger(MessageProcessorService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.output-topic}")
    private String outputTopic;

    @KafkaListener(topics = "${app.kafka.input-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(
            String jsonMessage,

            @Header("original_filename") String originalFilename
    ) {
        logger.info("Mensaje JSON recibido (Archivo Origen: {}): {}", originalFilename, jsonMessage);

        try {
            kafkaTemplate.send(outputTopic, jsonMessage);
            logger.info("Mensaje de {} reenviado a Kafka-02.", originalFilename);

        } catch (Exception e) {
            logger.error("Error al procesar el mensaje del archivo {}: {}", originalFilename, e.getMessage());
        }
    }
}
