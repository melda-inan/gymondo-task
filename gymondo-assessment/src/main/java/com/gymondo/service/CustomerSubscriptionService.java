package com.gymondo.service;

import com.gymondo.model.dto.CustomerSubscriptionDto;
import com.gymondo.model.dto.SubscriptionRequestDto;
import com.gymondo.model.entity.CustomerSubscription;
import com.gymondo.model.entity.Product;

import java.util.List;

public interface CustomerSubscriptionService {

    CustomerSubscription generateCustomerSubscription(Product product, SubscriptionRequestDto subscriptionRequest);
    CustomerSubscriptionDto save(CustomerSubscription customerSubscription);
    List<CustomerSubscriptionDto> getByCustomer(Long customerId);
    List<CustomerSubscriptionDto> getByCustomerAndStatus(Long customerId, String status);
    CustomerSubscriptionDto getById(Long id);
    CustomerSubscriptionDto pause(Long id);
    CustomerSubscriptionDto unpause(Long id);
    CustomerSubscriptionDto cancel(Long id);
}
