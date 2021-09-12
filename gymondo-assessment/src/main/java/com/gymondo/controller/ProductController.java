package com.gymondo.controller;

import com.gymondo.model.dto.CustomerSubscriptionDto;
import com.gymondo.model.dto.ProductDto;
import com.gymondo.model.dto.SubscriptionRequestDto;
import com.gymondo.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/product")
@Api(tags = "Product APIs")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "Get Product By Id")
    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @ApiOperation(value = "List Products with or without Voucher Code")
    @GetMapping
    public List<ProductDto> listProducts(@RequestParam(required = false) String voucherCode) {
        return voucherCode != null ?
                productService.listProductsWithVoucherCode(voucherCode)
                : productService.listProducts();

    }

    @ApiOperation(value = "Subscribe to Product")
    @PostMapping("/{id}/subscribe")
    public CustomerSubscriptionDto subscribe(@PathVariable("id") Long productId, @RequestBody @Validated SubscriptionRequestDto subscriptionRequest) {
        return productService.subscribe(productId, subscriptionRequest);
    }

}
