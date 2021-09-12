package com.gymondo.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gymondo.model.enums.CustomerSubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerSubscriptionDto {
    private Long id;
    private CustomerDto customer;
    private ProductDto product;
    private VoucherDto voucher;
    private Integer durationMonth;
    private Double price;
    private Double tax;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean trial;
    private LocalDate trialStartDate;
    private LocalDate trialEndDate;
    private LocalDate pauseDate;

    @Enumerated(EnumType.STRING)
    private CustomerSubscriptionStatus status;

}
