package com.interview.intercorp.ms01.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "customer_invoice_record")
public class InvoiceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_filename", nullable = false)
    private String sourceFilename;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "processed_at", nullable = false)
    private Date processedAt;

    @Column(name = "json_data", columnDefinition = "TEXT", nullable = false)
    private String jsonData;

}
