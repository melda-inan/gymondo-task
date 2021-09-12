package com.gymondo.service;

import com.gymondo.model.entity.Subscription;
import com.gymondo.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository
    ) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Subscription getSubscriptionEntity(Long id) {
        Optional<Subscription> subscription = subscriptionRepository.findById(id);

        if (subscription.isEmpty()) {
            throw new EntityNotFoundException("No subscription found with id: " + id);
        }

        return subscription.get();
    }

}
