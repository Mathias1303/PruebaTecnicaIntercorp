package com.interview.intercorp.ms01.repository;

import com.interview.intercorp.ms01.model.InvoiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRecordRepository extends JpaRepository<InvoiceRecord, Long> {

}
