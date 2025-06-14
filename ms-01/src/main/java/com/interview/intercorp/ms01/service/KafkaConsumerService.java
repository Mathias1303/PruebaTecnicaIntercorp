package com.interview.intercorp.ms01.service;

import com.interview.intercorp.ms01.model.InvoiceRecord;
import com.interview.intercorp.ms01.repository.InvoiceRecordRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Autowired
    private InvoiceRecordRepository repository;

    @KafkaListener(topics = "${app.kafka.input-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeAndSave(ConsumerRecord<String, String> record, Acknowledgment ack) {

        String jsonMessage = record.value();
        String originalFilename = "desconocido";
        if (record.headers().lastHeader("original_filename") != null) {
            originalFilename = new String(record.headers().lastHeader("original_filename").value());
        }
        logger.info("Mensaje recibido de Kafka (Archivo Origen: {}). Procediendo a guardar en PostgreSQL...", originalFilename);
        try {
            InvoiceRecord invoiceRecord = new InvoiceRecord();
            invoiceRecord.setSourceFilename(originalFilename);
            invoiceRecord.setProcessedAt(new java.util.Date());
            invoiceRecord.setJsonData(jsonMessage);

            repository.save(invoiceRecord);
            logger.info("Registro guardado en PostgreSQL (Archivo Origen: {}).", originalFilename);
            ack.acknowledge();

        } catch (Exception e) {
            logger.error("Error al guardar en PostgreSQL (Archivo Origen: {}): {}", originalFilename, e.getMessage());
        }
    }
}
