package com.gymondo.model.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "Id of the customer who wants to subscribe", example = "1")
    private Long customerId;

    @NonNull
    @ApiModelProperty(value = "Id of the subscription that belongs to product", example = "1")
    private Long subscriptionId;

    @ApiModelProperty(value = "Vourcher's code information", example = "WELCOMENEWBY")
    private String voucherCode;

    @ApiModelProperty(value = "Indicates if this is a trial subscription", example = "false")
    private Boolean trial = false;
}
