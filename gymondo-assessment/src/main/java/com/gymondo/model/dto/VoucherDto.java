package com.gymondo.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gymondo.model.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoucherDto {
    private Long id;

    private String code;

    @Enumerated(EnumType.ORDINAL)
    private DiscountType discountType;

    private Integer discountPercentage;

    private Double discountAmount;
}
