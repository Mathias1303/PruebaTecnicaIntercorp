package com.interview.intercorp.ms04.repository;

import com.interview.intercorp.ms04.model.InvoiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRecordRepository extends JpaRepository<InvoiceRecord, Long> {

    @Query(value = "SELECT * FROM customer_invoice_record WHERE lower(CAST(json_data AS json) -> 'root' -> 'person' ->> 'firstname') = lower(:name)", nativeQuery = true)
    List<InvoiceRecord> findByPersonFirstNameInJson(@Param("name") String name);

    @Query(value = "SELECT * FROM customer_invoice_record " +
            "WHERE CAST(json_data AS json) -> 'root' -> 'person' ->> 'email' = :email LIMIT 1", nativeQuery = true)
    Optional<InvoiceRecord> findByPersonEmailInJson(@Param("email") String email);

    @Query("SELECT MAX(ir.processedAt) FROM InvoiceRecord ir")
    Date findLatestProcessedAt();
}
