package com.gymondo.model.mapper;

import com.gymondo.model.dto.CustomerSubscriptionDto;
import com.gymondo.model.entity.CustomerSubscription;
import com.gymondo.model.entity.Product;
import com.gymondo.model.enums.CustomerSubscriptionStatus;

public final class CustomerSubscriptionMapper {

    public static CustomerSubscriptionDto toDto(CustomerSubscription customerSubscription) {
        if (customerSubscription == null) {
            return null;
        }

        CustomerSubscriptionDto customerSubscriptionDto = new CustomerSubscriptionDto();
        customerSubscriptionDto.setId(customerSubscription.getId());
        customerSubscriptionDto.setCustomer(CustomerMapper.toDto(customerSubscription.getCustomer()));

        Product product = customerSubscription.getSubscription().getProduct();
        product.setSubscriptions(null); //no need for this information
        product.setVouchers(null); //no need for this information
        customerSubscriptionDto.setProduct(ProductMapper.toDto(product));

        customerSubscriptionDto.setDurationMonth(customerSubscription.getSubscription().getMonthDuration());
        customerSubscriptionDto.setPrice(customerSubscription.getPrice());
        customerSubscriptionDto.setStartDate(customerSubscription.getStartDate());
        customerSubscriptionDto.setEndDate(customerSubscription.getEndDate());
        customerSubscriptionDto.setTax(customerSubscription.getTax());
        customerSubscriptionDto.setVoucher(VoucherMapper.toDto(customerSubscription.getVoucher()));
        customerSubscriptionDto.setStatus(CustomerSubscriptionStatus.fromValue(customerSubscription.getStatus()));
        customerSubscriptionDto.setTrial(customerSubscription.getTrial());
        customerSubscriptionDto.setTrialStartDate(customerSubscription.getTrialStartDate());
        customerSubscriptionDto.setTrialEndDate(customerSubscription.getTrialEndDate());
        customerSubscriptionDto.setPauseDate(customerSubscription.getPauseDate());

        return customerSubscriptionDto;
    }
}
