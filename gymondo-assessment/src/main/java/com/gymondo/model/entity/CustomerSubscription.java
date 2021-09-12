package com.gymondo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "customer_subscription")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    private Double price;

    private Double tax;

    private LocalDate startDate;

    private LocalDate endDate;

    private String status;

    private Boolean trial;

    private LocalDate trialStartDate;

    private LocalDate trialEndDate;

    private LocalDate pauseDate;
}
