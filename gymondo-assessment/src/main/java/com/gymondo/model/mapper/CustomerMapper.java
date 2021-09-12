package com.gymondo.model.mapper;

import com.gymondo.model.dto.CustomerDto;
import com.gymondo.model.entity.Customer;

public final class CustomerMapper {

    public static Customer fromId(Long id) {
        if (id == null) {
            return null;
        }

        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }

    public static CustomerDto toDto(Customer customer) {
        if (customer == null) {
            return null;
        }

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setEmail(customer.getEmail());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        return customerDto;
    }

    public static Customer toEntity(CustomerDto customerDto) {
        if (customerDto == null) {
            return null;
        }

        Customer customer = new Customer();
        customer.setId(customerDto.getId());
        customer.setEmail(customerDto.getEmail());
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        return customer;
    }
}
