package com.gymondo.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequestDto {

    @NotNull(message = "customerId can not be null.")
    @ApiModelProperty(value = "Id of the customer who wants to subscribe", example = "1", required = true)
    private Long customerId;

    @NotNull
    @ApiModelProperty(value = "Id of the subscription that belongs to product", example = "1", required = true)
    private Long subscriptionId;

    @ApiModelProperty(value = "Voucher's code information", example = "WELCOMENEWBY")
    private String voucherCode;

    @ApiModelProperty(value = "Indicates if this is a trial subscription", example = "false")
    private Boolean trial = false;
}
