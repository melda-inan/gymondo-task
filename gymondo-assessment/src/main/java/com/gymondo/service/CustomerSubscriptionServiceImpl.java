package com.gymondo.service;

import com.gymondo.model.dto.CustomerSubscriptionDto;
import com.gymondo.model.dto.SubscriptionRequestDto;
import com.gymondo.model.entity.*;
import com.gymondo.model.enums.CustomerSubscriptionStatus;
import com.gymondo.model.enums.DiscountType;
import com.gymondo.model.mapper.CustomerSubscriptionMapper;
import com.gymondo.repository.CustomerSubscriptionRepository;
import com.gymondo.util.DateUtil;
import com.gymondo.util.PriceCalculator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerSubscriptionServiceImpl implements CustomerSubscriptionService {

    @Value("${gymondo.subscription.trial.duration}")
    private long trialDuration;

    @Value("${gymondo.subscription.trial.duration.type}")
    private String trialDurationType;

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

        Subscription subscription = getAndValidateSubscription(subscriptionRequest.getSubscriptionId(), product);

        CustomerSubscription customerSubscription = new CustomerSubscription();
        customerSubscription.setSubscription(subscription);
        customerSubscription.setCustomer(customer);

        Voucher voucher = getAndValidateVoucher(subscriptionRequest.getVoucherCode(), product);

        customerSubscription.setVoucher(voucher);

        Double price = calculatePrice(subscription.getPrice(), voucher);

        customerSubscription.setPrice(price);
        customerSubscription.setTax(PriceCalculator.calculateTax(price, subscription.getTaxPercentage()));

        customerSubscription.setStatus(CustomerSubscriptionStatus.ACTIVE.getValue());

        customerSubscription.setTrial(subscriptionRequest.getTrial());

        setDates(customerSubscription, subscription.getMonthDuration());

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
            throw new IllegalArgumentException(String.format("You can't pause non-active subscription, subscription status: %s", CustomerSubscriptionStatus.fromValue(customerSubscription.getStatus()).name()));
        } else if (customerSubscription.getTrial()) {
            throw new IllegalArgumentException(String.format("You can't pause subscription during trial."));
        }

        customerSubscription.setStatus(CustomerSubscriptionStatus.PAUSED.getValue());
        customerSubscription.setPauseDate(LocalDate.now());
        customerSubscription = customerSubscriptionRepository.save(customerSubscription);
        return CustomerSubscriptionMapper.toDto(customerSubscription);
    }

    @Override
    public CustomerSubscriptionDto unpause(Long id) {
        CustomerSubscription customerSubscription = getEntityById(id);
        if (!customerSubscription.getStatus().equals(CustomerSubscriptionStatus.PAUSED.getValue())) {
            throw new IllegalArgumentException(String.format("Subscription is not paused, subscription status: %s", CustomerSubscriptionStatus.fromValue(customerSubscription.getStatus()).name()));
        }
        customerSubscription.setStatus(CustomerSubscriptionStatus.ACTIVE.getValue());

        LocalDate today = LocalDate.now();
        LocalDate pauseDate = customerSubscription.getPauseDate();
        long pausedDayDuration = DateUtil.getDayDifference(pauseDate, today);

        LocalDate newEndDate = customerSubscription.getEndDate().plusDays(pausedDayDuration);
        customerSubscription.setEndDate(newEndDate);
        customerSubscription.setPauseDate(null);

        customerSubscription = customerSubscriptionRepository.save(customerSubscription);
        return CustomerSubscriptionMapper.toDto(customerSubscription);
    }

    @Override
    public CustomerSubscriptionDto cancel(Long id) {
        CustomerSubscription customerSubscription = getEntityById(id);
        if (!customerSubscription.getStatus().equals(CustomerSubscriptionStatus.ACTIVE.getValue())) {
            throw new IllegalArgumentException(String.format("You can't cancel non-active subscription, subscription status: %s", CustomerSubscriptionStatus.fromValue(customerSubscription.getStatus()).name()));
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

    private Double calculatePrice(Double price, Voucher voucher) {
        if (voucher != null) {
            price = voucher.getDiscountType() == DiscountType.PERCENTAGE.getValue() ?
                    PriceCalculator.calculateDiscountPrice(price, voucher.getDiscountPercentage())
                    : PriceCalculator.calculateDiscountPrice(price, voucher.getDiscountAmount());


        }

        return price;
    }

    private Subscription getAndValidateSubscription(Long subscriptionId, Product product) {
        Subscription subscription = subscriptionService.getSubscriptionEntity(subscriptionId);

        if (subscription.getProduct().getId() != product.getId()) {
            throw new IllegalArgumentException(String.format("Subscription with id: %s can not be used for product: %s", subscription.getId(), product.getName()));
        }

        return subscription;
    }

    private Voucher getAndValidateVoucher(String voucherCode, Product product) {
        Voucher voucher = null;
        if (voucherCode != null) {
            voucher = voucherService.getVoucherEntityByCode(voucherCode);
            boolean isValidVoucherForProduct = product.getVouchers().stream()
                    .anyMatch(it -> it.getCode().equals(voucherCode));

            if (!isValidVoucherForProduct) {
                throw new IllegalArgumentException(String.format("Voucher code: %s can not be used for product %s", voucher.getCode(), product.getName()));
            }

        }

        return voucher;
    }

    private void setDates(CustomerSubscription customerSubscription, int subscriptionDuration) {
        LocalDate today = LocalDate.now();
        if (customerSubscription.getTrial()) {
            customerSubscription.setTrialStartDate(today);
            LocalDate trialEndDate = today.plus(trialDuration, ChronoUnit.valueOf(trialDurationType));
            customerSubscription.setTrialEndDate(trialEndDate);

            LocalDate subscriptionEndDate = trialEndDate.plusMonths(subscriptionDuration);
            customerSubscription.setStartDate(trialEndDate);
            customerSubscription.setEndDate(subscriptionEndDate);
        } else {
            LocalDate endDate = today.plusMonths(subscriptionDuration);
            customerSubscription.setStartDate(today);
            customerSubscription.setEndDate(endDate);
        }
    }
}
