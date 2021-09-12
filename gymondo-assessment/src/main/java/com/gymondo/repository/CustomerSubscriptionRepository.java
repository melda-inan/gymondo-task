package com.gymondo.repository;

import com.gymondo.model.entity.CustomerSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerSubscriptionRepository extends JpaRepository<CustomerSubscription, Long> {

    CustomerSubscription findByCustomerIdAndSubscriptionIdAndStatusIn(Long customerId, Long subscriptionId, List<String> status);
    List<CustomerSubscription> findByCustomerId(Long customerId);
    List<CustomerSubscription> findByCustomerIdAndStatus(Long customerId, String status);
}
