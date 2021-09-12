package com.gymondo.controller;

import com.gymondo.model.dto.CustomerSubscriptionDto;
import com.gymondo.service.CustomerSubscriptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "Get Customer's Subscriptions - List customer's all subscriptions or list by status")
    @GetMapping
    public List<CustomerSubscriptionDto> getCustomerSubscriptionsByCustomer(@ApiParam(value = "Id of the customer", example = "1") @RequestParam("customerId") Long customerId,
                                                                            @ApiParam(value = "Status can be ACTIVE,PAUSED,CANCELED", example = "ACTIVE") @RequestParam (value = "status" , required = false) String status) {
        return status != null ?
                customerSubscriptionService.getByCustomerAndStatus(customerId, status)
                : customerSubscriptionService.getByCustomer(customerId);
    }

    @ApiOperation(value = "Get Subscription by Id")
    @GetMapping("/{subscriptionId}")
    public CustomerSubscriptionDto getCustomerSubscriptionById(@PathVariable("subscriptionId") Long subscriptionId) {
        return customerSubscriptionService.getById(subscriptionId);
    }

    @ApiOperation(value = "Pause Subscription")
    @PostMapping("/{subscriptionId}/pause")
    public CustomerSubscriptionDto pauseSubscription(@PathVariable Long subscriptionId) {
        return customerSubscriptionService.pause(subscriptionId);
    }

    @ApiOperation(value = "Unpause Subscriptions")
    @PostMapping("/{subscriptionId}/unpause")
    public CustomerSubscriptionDto unpauseSubscription(@PathVariable Long subscriptionId) {
        return customerSubscriptionService.unpause(subscriptionId);
    }

    @ApiOperation(value = "Cancel Subscriptions")
    @PostMapping("/{subscriptionId}/cancel")
    public CustomerSubscriptionDto cancelSubscription(@PathVariable Long subscriptionId) {
        return customerSubscriptionService.cancel(subscriptionId);
    }

}
