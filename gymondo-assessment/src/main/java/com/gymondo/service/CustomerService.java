package com.gymondo.service;

import com.gymondo.model.dto.CustomerDto;
import com.gymondo.model.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer getCustomerEntity(Long id);

    List<CustomerDto> listCustomers();
}
