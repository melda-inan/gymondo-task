package com.gymondo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequestDto {

    @NonNull
    private Long customerId;

    @NonNull
    private Long subscriptionId;

    private String voucherCode;
}
