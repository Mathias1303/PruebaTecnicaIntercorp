package com.interview.intercorp.ms05.ms05.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class XmlProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(XmlProcessingService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String kafkaTopic;

    @Value("${app.xml.processed-dir}")
    private String processedDir;

    @Value("${app.xml.error-dir}")
    private String errorDir;

    public void processSingleFile(Path filePath) {
        try {
            String xmlContent = Files.readString(filePath);
            JSONObject jsonObject = XML.toJSONObject(xmlContent);
            String jsonString = jsonObject.toString(4);
            String originalFilename = filePath.getFileName().toString();

            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
                    kafkaTopic,
                    jsonString
            );
            producerRecord.headers().add(new RecordHeader(
                    "original_filename",
                    originalFilename.getBytes(StandardCharsets.UTF_8)
            ));
            kafkaTemplate.send(producerRecord);
            logger.info("Archivo {} procesado y enviado a Kafka con cabecera.", originalFilename);
            moveFile(filePath, processedDir);
        } catch (Exception e) {
            logger.error("Error al procesar el archivo {}: {}", filePath.getFileName(), e.getMessage());
            moveFile(filePath, errorDir);
        }
    }

    private void moveFile(Path sourcePath, String targetDir) {
        try {
            Path targetPath = Paths.get(targetDir).resolve(sourcePath.getFileName());
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Archivo {} movido a {}", sourcePath.getFileName(), targetDir);
        } catch (IOException e) {
            logger.error("No se pudo mover el archivo {}: {}", sourcePath.getFileName(), e.getMessage());
        }
    }
}
