package com.gymondo.controller;

import com.gymondo.model.dto.CustomerDto;
import com.gymondo.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/customer")
@Api(tags = "Customer APIs")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @ApiOperation("List All Customers")
    public List<CustomerDto> getCustomers() {
        return customerService.listCustomers();
    }
}
