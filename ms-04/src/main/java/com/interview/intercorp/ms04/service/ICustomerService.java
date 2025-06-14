package com.interview.intercorp.ms04.service;

import com.interview.intercorp.ms04.dto.CustomerDto;
import com.interview.intercorp.ms04.dto.PersonListResponseDto;

import java.util.List;

public interface ICustomerService {

    List<CustomerDto> findPersonsByName(String name);
    PersonListResponseDto getAllPersons();

}
