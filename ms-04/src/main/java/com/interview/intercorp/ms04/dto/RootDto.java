package com.interview.intercorp.ms04.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RootDto {

    private String date;
    private int random;
    private String regEx;

    @JsonProperty("random_float")
    private double randomFloat;
    private boolean bool;
    private CustomerDto person;
    private List<String> elt = new ArrayList<>();

    @JsonProperty("enum")
    private String enumValue;

    private Map<String, Object> dynamicProperties = new HashMap<>();

    @JsonAnySetter
    public void addDynamicProperty(String key, Object value) {
        dynamicProperties.put(key, value);
    }

    public CustomerDto getPerson() {
        return person;
    }
}
