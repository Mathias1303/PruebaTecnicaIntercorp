package com.interview.intercorp.ms04.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.intercorp.ms04.model.InvoiceRecord;
import com.interview.intercorp.ms04.repository.InvoiceRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DataComparisonService {

    @Autowired
    private InvoiceRecordRepository invoiceRecordRepository;
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void compareKafkaMessageWithDb(String kafkaJsonString) {
        try {
            JsonNode kafkaNode = objectMapper.readTree(kafkaJsonString);
            String email = kafkaNode.path("root").path("person").path("email").asText();
            if (email == null || email.isEmpty()) {
                logger.warn("El JSON de Kafka no contiene un email para usar como identificador. Abortando comparación.");
                return;
            }
            logger.info("Buscando en BD el registro para el email: {}", email);
            Optional<InvoiceRecord> dbRecordOptional = invoiceRecordRepository.findByPersonEmailInJson(email);
            if (dbRecordOptional.isEmpty()) {
                logger.warn("Mensaje para '{}' recibido en Kafka, pero NO se encontró en la Base de Datos.", email);
            } else {
                String dbJsonString = dbRecordOptional.get().getJsonData();
                JsonNode dbNode = objectMapper.readTree(dbJsonString);
                if (kafkaNode.equals(dbNode)) {
                    logger.info("Los datos para '{}' son IDÉNTICOS en Kafka y en la Base de Datos.", email);
                } else {
                    logger.error("El JSON para '{}' es diferente entre Kafka y la Base de Datos.", email);
                }
            }
        } catch (JsonProcessingException e) {
            logger.error("Error fatal al parsear el JSON. No se puede realizar la comparación.", e);
        }
    }
}
