package com.gymondo.service;

import com.gymondo.model.entity.Customer;
import com.gymondo.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer getCustomerEntity(Long id) {
        Customer customer = customerRepository.getById(id);

        if (customer == null) {
            throw new EntityNotFoundException(String.format("No customer is found with id: %s", id));
        }

        return customer;
    }
}
