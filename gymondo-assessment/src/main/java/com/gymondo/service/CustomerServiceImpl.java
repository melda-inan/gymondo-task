package com.gymondo.service;

import com.gymondo.model.dto.CustomerDto;
import com.gymondo.model.entity.Customer;
import com.gymondo.model.mapper.CustomerMapper;
import com.gymondo.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<CustomerDto> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(CustomerMapper::toDto)
                .collect(Collectors.toList());
    }
}
