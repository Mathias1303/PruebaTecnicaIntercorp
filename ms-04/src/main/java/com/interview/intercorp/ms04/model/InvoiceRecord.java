package com.interview.intercorp.ms04.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

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

    public Long getId() {
        return id;
    }

    public String getJsonData() {
        return jsonData;
    }
}
