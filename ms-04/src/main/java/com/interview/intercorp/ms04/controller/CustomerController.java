package com.interview.intercorp.ms04.controller;

import com.interview.intercorp.ms04.dto.CustomerDto;
import com.interview.intercorp.ms04.dto.PersonListResponseDto;
import com.interview.intercorp.ms04.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/search")
    public ResponseEntity<List<CustomerDto>> searchPersonsByName(@RequestParam String name) {
        List<CustomerDto> foundPersons = customerService.findPersonsByName(name);
        if (foundPersons.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundPersons);
    }

    @GetMapping("/list")
    public ResponseEntity<PersonListResponseDto> listAllPersons() {
        PersonListResponseDto response = customerService.getAllPersons();
        return ResponseEntity.ok(response);
    }
}
