package com.gymondo.service;

import com.gymondo.model.dto.CustomerSubscriptionDto;
import com.gymondo.model.dto.ProductDto;
import com.gymondo.model.dto.SubscriptionRequestDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> listProducts();

    List<ProductDto> listProductsWithVoucherCode(String voucherCode);

    ProductDto getProduct(Long id);

    CustomerSubscriptionDto subscribe(Long productId, SubscriptionRequestDto subscriptionRequest);

}
