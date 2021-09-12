package com.gymondo.controller;

import com.gymondo.model.dto.CustomerSubscriptionDto;
import com.gymondo.service.CustomerSubscriptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/subscription")
@Api(tags = "Customer Subscription APIs")
public class SubscriptionController {

    private final CustomerSubscriptionService customerSubscriptionService;

    public SubscriptionController(CustomerSubscriptionService customerSubscriptionService) {
        this.customerSubscriptionService = customerSubscriptionService;
    }

    @ApiOperation(value = "Get Customer's Subscriptions")
    @GetMapping
    public List<CustomerSubscriptionDto> getCustomerSubscriptionsByCustomer(@RequestParam("customerId") Long customerId,
                                                                            @RequestParam (value = "status" , required = false) String status) {
        return status != null ?
                customerSubscriptionService.getByCustomerAndStatus(customerId, status)
                : customerSubscriptionService.getByCustomer(customerId);
    }

    @ApiOperation(value = "Get Subscription by Id")
    @GetMapping("/{id}")
    public CustomerSubscriptionDto getCustomerSubscriptionById(@PathVariable Long id) {
        return customerSubscriptionService.getById(id);
    }

    @ApiOperation(value = "Pause Subscription")
    @PostMapping("/{id}/pause")
    public CustomerSubscriptionDto pauseSubscription(@PathVariable Long id) {
        return customerSubscriptionService.pause(id);
    }

    @ApiOperation(value = "Unpause Subscriptions")
    @PostMapping("/{id}/unpause")
    public CustomerSubscriptionDto unpauseSubscription(@PathVariable Long id) {
        return customerSubscriptionService.unpause(id);
    }

    @ApiOperation(value = "Cancel Subscriptions")
    @PostMapping("/{id}/cancel")
    public CustomerSubscriptionDto cancelSubscription(@PathVariable Long id) {
        return customerSubscriptionService.cancel(id);
    }

}
