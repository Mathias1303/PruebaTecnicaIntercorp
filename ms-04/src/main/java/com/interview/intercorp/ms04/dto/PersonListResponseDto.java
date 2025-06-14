package com.interview.intercorp.ms04.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

public class PersonListResponseDto {

    private Date lastUpdated;
    private List<CustomerDto> data;

    // 2. Constructor con todos los argumentos (el que tu código necesita)
    public PersonListResponseDto(Date lastUpdated, List<CustomerDto> data) {
        this.lastUpdated = lastUpdated;
        this.data = data;
    }

    // 3. Getters y Setters manuales
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<CustomerDto> getData() {
        return data;
    }

    public void setData(List<CustomerDto> data) {
        this.data = data;
    }
}
