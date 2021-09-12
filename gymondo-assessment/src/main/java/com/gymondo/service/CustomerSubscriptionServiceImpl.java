package com.gymondo.service;

import com.gymondo.model.dto.CustomerSubscriptionDto;
import com.gymondo.model.dto.SubscriptionRequestDto;
import com.gymondo.model.entity.*;
import com.gymondo.model.enums.CustomerSubscriptionStatus;
import com.gymondo.model.enums.DiscountType;
import com.gymondo.model.mapper.CustomerSubscriptionMapper;
import com.gymondo.repository.CustomerSubscriptionRepository;
import com.gymondo.util.PriceCalculator;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerSubscriptionServiceImpl implements CustomerSubscriptionService {

    private final CustomerSubscriptionRepository customerSubscriptionRepository;
    private final SubscriptionService subscriptionService;
    private final VoucherService voucherService;
    private final CustomerService customerService;


    public CustomerSubscriptionServiceImpl(CustomerSubscriptionRepository customerSubscriptionRepository,
                                           SubscriptionService subscriptionService,
                                           VoucherService voucherService,
                                           CustomerService customerService) {
        this.customerSubscriptionRepository = customerSubscriptionRepository;
        this.subscriptionService = subscriptionService;
        this.voucherService = voucherService;
        this.customerService = customerService;
    }

    @Override
    public CustomerSubscription generateCustomerSubscription(Product product, SubscriptionRequestDto subscriptionRequest) {
        checkIfExist(subscriptionRequest.getCustomerId(), subscriptionRequest.getSubscriptionId());

        Customer customer = customerService.getCustomerEntity(subscriptionRequest.getCustomerId());

        Subscription subscription = subscriptionService.getSubscriptionEntity(subscriptionRequest.getSubscriptionId());

        if (subscription.getProduct().getId() != product.getId()) {
            throw new IllegalArgumentException(String.format("Subscription with id: %s can not be used for product with id: %s", subscription.getId(), product.getId()));
        }

        Voucher voucher = null;
        if (subscriptionRequest.getVoucherCode() != null) {
            voucher = voucherService.getVoucherEntityByCode(subscriptionRequest.getVoucherCode());
            boolean isValidVoucherForProduct = product.getVouchers().stream()
                    .anyMatch(it -> it.getCode().equals(subscriptionRequest.getVoucherCode()));

            if (!isValidVoucherForProduct) {
                throw new IllegalArgumentException(String.format("Voucher with id: %s can not be used for product with id: %s", voucher.getId(), product.getId()));
            }
        }

        CustomerSubscription customerSubscription = new CustomerSubscription();
        customerSubscription.setSubscription(subscription);
        customerSubscription.setCustomer(customer);

        Double price = subscription.getPrice();

        if (voucher != null) {
            price = voucher.getDiscountType() == DiscountType.PERCENTAGE.getValue() ?
                    PriceCalculator.calculateDiscountPrice(price, voucher.getDiscountPercentage())
                    : PriceCalculator.calculateDiscountPrice(price, voucher.getDiscountAmount());

            customerSubscription.setVoucher(voucher);
        }

        customerSubscription.setPrice(price);
        customerSubscription.setTax(PriceCalculator.calculateTax(price, subscription.getTaxPercentage()));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(subscription.getMonthDuration());
        customerSubscription.setStartDate(startDate);
        customerSubscription.setEndDate(endDate);

        customerSubscription.setStatus(CustomerSubscriptionStatus.ACTIVE.getValue());

        return customerSubscription;
    }

    @Override
    public CustomerSubscriptionDto save(CustomerSubscription customerSubscription) {
        CustomerSubscription subscription = customerSubscriptionRepository.save(customerSubscription);
        return CustomerSubscriptionMapper.toDto(subscription);
    }

    @Override
    public List<CustomerSubscriptionDto> getByCustomer(Long customerId) {
        return customerSubscriptionRepository.findByCustomerId(customerId)
                .stream()
                .map(CustomerSubscriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerSubscriptionDto> getByCustomerAndStatus(Long customerId, String status) {
        return customerSubscriptionRepository.findByCustomerIdAndStatus(customerId, CustomerSubscriptionStatus.valueOf(status).getValue())
                .stream()
                .map(CustomerSubscriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerSubscriptionDto getById(Long id) {
        CustomerSubscription customerSubscription = getEntityById(id);
        return CustomerSubscriptionMapper.toDto(customerSubscription);
    }

    @Override
    public CustomerSubscriptionDto pause(Long id) {
        CustomerSubscription customerSubscription = getEntityById(id);
        if (!customerSubscription.getStatus().equals(CustomerSubscriptionStatus.ACTIVE.getValue())) {
            throw new IllegalArgumentException(String.format("You can't pause non-active subscription, subscription status: ", CustomerSubscriptionStatus.fromValue(customerSubscription.getStatus())));
        }

        customerSubscription.setStatus(CustomerSubscriptionStatus.PAUSED.getValue());
        customerSubscription = customerSubscriptionRepository.save(customerSubscription);
        return CustomerSubscriptionMapper.toDto(customerSubscription);
    }

    @Override
    public CustomerSubscriptionDto unpause(Long id) {
        CustomerSubscription customerSubscription = getEntityById(id);
        if (!customerSubscription.getStatus().equals(CustomerSubscriptionStatus.PAUSED.getValue())) {
            throw new IllegalArgumentException(String.format("Subscription is not paused, subscription status: ", CustomerSubscriptionStatus.fromValue(customerSubscription.getStatus())));
        }
        customerSubscription.setStatus(CustomerSubscriptionStatus.ACTIVE.getValue());
        customerSubscription = customerSubscriptionRepository.save(customerSubscription);
        return CustomerSubscriptionMapper.toDto(customerSubscription);
    }

    @Override
    public CustomerSubscriptionDto cancel(Long id) {
        CustomerSubscription customerSubscription = getEntityById(id);
        if (customerSubscription.getStatus().equals(CustomerSubscriptionStatus.CANCELED.getValue())) {
            throw new IllegalArgumentException("Subscription is already canceled.");
        }

        customerSubscription.setStatus(CustomerSubscriptionStatus.CANCELED.getValue());
        customerSubscription = customerSubscriptionRepository.save(customerSubscription);
        return CustomerSubscriptionMapper.toDto(customerSubscription);
    }

    private CustomerSubscription getEntityById(Long id) {
        CustomerSubscription customerSubscription = customerSubscriptionRepository.getById(id);

        if (customerSubscription == null) {
            throw new EntityNotFoundException(String.format("No customer subscription found with id: %s", id));
        }

        return customerSubscription;
    }

    private void checkIfExist(Long customerId, Long subscriptionId) {
        List<String> statusList = Arrays.asList(CustomerSubscriptionStatus.ACTIVE.getValue(),
                                                CustomerSubscriptionStatus.PAUSED.getValue());
        CustomerSubscription customerSubscription = customerSubscriptionRepository.findByCustomerIdAndSubscriptionIdAndStatusIn(customerId, subscriptionId, statusList);
        if (customerSubscription != null) {
            throw new EntityExistsException(String.format("You already have subscription for this product, subscriptionId: %s, subscription status: %s",
                    subscriptionId, CustomerSubscriptionStatus.fromValue(customerSubscription.getStatus())));
        }


    }
}
