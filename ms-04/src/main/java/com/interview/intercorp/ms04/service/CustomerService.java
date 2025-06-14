package com.interview.intercorp.ms04.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.intercorp.ms04.dto.CustomerDto;
import com.interview.intercorp.ms04.dto.PersonListResponseDto;
import com.interview.intercorp.ms04.dto.RootDto;
import com.interview.intercorp.ms04.model.InvoiceRecord;
import com.interview.intercorp.ms04.repository.InvoiceRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final InvoiceRecordRepository invoiceRecordRepository;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    public CustomerService(InvoiceRecordRepository invoiceRecordRepository, ObjectMapper objectMapper) {
        this.invoiceRecordRepository = invoiceRecordRepository;
        this.objectMapper = objectMapper;
    }

    public List<CustomerDto> findPersonsByName(String name) {
        List<InvoiceRecord> foundRecords = invoiceRecordRepository.findByPersonFirstNameInJson(name);
        return foundRecords.stream()
                .map(this::extractPersonFromRecord)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public PersonListResponseDto getAllPersons() {
        List<InvoiceRecord> allRecords = invoiceRecordRepository.findAll();
        List<CustomerDto> personList = allRecords.stream()
                .map(this::extractPersonFromRecord)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        Date lastUpdateDate = invoiceRecordRepository.findLatestProcessedAt();
        return new PersonListResponseDto(lastUpdateDate, personList);
    }

    private Optional<CustomerDto> extractPersonFromRecord(InvoiceRecord record) {
        try {
            JsonNode fullJsonTree = objectMapper.readTree(record.getJsonData());
            JsonNode rootNode = fullJsonTree.path("root");
            if (rootNode.isMissingNode()) {
                logger.error("El JSON del registro con ID {} no contiene la llave 'root' de nivel superior.", record.getId());
                return Optional.empty();
            }
            RootDto root = objectMapper.treeToValue(rootNode, RootDto.class);
            return Optional.ofNullable(root.getPerson());

        } catch (JsonProcessingException e) {
            logger.error("No se pudo parsear el JSON para el registro con ID: {}", record.getId(), e);
            return Optional.empty();
        }
    }
}
