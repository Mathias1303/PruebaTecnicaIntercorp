package com.interview.intercorp.ms04.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    private DataComparisonService dataService;

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "${app.kafka.input-topic}", groupId = "ms04-invoice-consumer-group")
    public void consume(String jsonMessage) {
        logger.info("===> Mensaje JSON recibido desde el tópico '${app.kafka.input-topic}'. Iniciando validación...");
        dataService.compareKafkaMessageWithDb(jsonMessage);
    }
}
