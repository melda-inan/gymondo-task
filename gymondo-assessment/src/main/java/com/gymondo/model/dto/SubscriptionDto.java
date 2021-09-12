package com.gymondo.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionDto {

    private Long id;
    private Integer monthDuration;
    private Double price;
    private Integer taxPercentage;
    private Double tax;
    private ProductDto product;
}
