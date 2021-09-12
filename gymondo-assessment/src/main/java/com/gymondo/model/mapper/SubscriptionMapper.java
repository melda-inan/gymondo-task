package com.gymondo.model.mapper;

import com.gymondo.model.dto.SubscriptionDto;
import com.gymondo.model.entity.Subscription;
import com.gymondo.util.PriceCalculator;

public final class SubscriptionMapper {

    public static Subscription fromId(Long id) {
        if (id == null) {
            return null;
        }

        Subscription subscription = new Subscription();
        subscription.setId(id);
        return subscription;
    }

    public static Subscription toEntity(SubscriptionDto subscriptionDto) {
        if (subscriptionDto == null) {
            return null;
        }

        Subscription subscription = new Subscription();
        subscription.setId(subscriptionDto.getId());
        subscription.setPrice(subscriptionDto.getPrice());
        subscription.setMonthDuration(subscriptionDto.getMonthDuration());
        subscription.setTaxPercentage(subscriptionDto.getTaxPercentage());

        if(subscriptionDto.getProduct() != null) {
            subscriptionDto.getProduct().setSubscriptions(null);
            subscription.setProduct(ProductMapper.toEntity(subscriptionDto.getProduct()));
        }

        return subscription;
    }

    public static SubscriptionDto toDto(Subscription subscription) {
        if (subscription == null) {
            return null;
        }

        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setId(subscription.getId());
        subscriptionDto.setPrice(subscription.getPrice());
        subscriptionDto.setMonthDuration(subscription.getMonthDuration());
        subscriptionDto.setTaxPercentage(subscription.getTaxPercentage());

        subscriptionDto.setTax(PriceCalculator.calculateTax(subscription.getPrice(), subscription.getTaxPercentage()));

        if(subscription.getProduct() != null) {
            subscription.getProduct().setSubscriptions(null);
            subscriptionDto.setProduct(ProductMapper.toDto(subscription.getProduct()));
        }

        return subscriptionDto;
    }
}
