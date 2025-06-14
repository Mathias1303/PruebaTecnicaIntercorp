package com.interview.intercorp.ms05.ms05.scheduler;

import com.interview.intercorp.ms05.ms05.service.XmlProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class XmlFileScheduler {

    private static final Logger logger = LoggerFactory.getLogger(XmlFileScheduler.class);

    @Autowired
    private XmlProcessingService processingService;

    @Value("${app.xml.input-dir}")
    private String inputDir;

    @Scheduled(fixedRate = 180000)
    public void scheduleXmlFileProcessing() {
        logger.info("Iniciando escaneo de archivos XML en: {}", inputDir);
        Path inputPath = Paths.get(inputDir);

        try (Stream<Path> paths = Files.list(inputPath)) {
            paths.filter(path -> path.toString().toLowerCase().endsWith(".xml"))
                    .findFirst()
                    .ifPresent(processingService::processSingleFile);
        } catch (IOException e) {
            logger.error("Error al leer el directorio de entrada: {}", inputDir, e);
        }
        logger.info("Escaneo de archivos XML finalizado.");
    }
}
