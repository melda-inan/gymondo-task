package com.gymondo.controller;

import com.gymondo.model.dto.CustomerSubscriptionDto;
import com.gymondo.model.dto.ProductDto;
import com.gymondo.model.dto.SubscriptionRequestDto;
import com.gymondo.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }

    @ApiOperation(value = "List Products - List all products or list products by voucher code")
    @GetMapping
    public List<ProductDto> listProducts(@RequestParam(required = false) String voucherCode) {
        return voucherCode != null ?
                productService.listProductsWithVoucherCode(voucherCode)
                : productService.listProducts();

    }

    @ApiOperation(value = "Subscribe to Product - Buy/subscribe to product for given customer")
    @PostMapping("/{productId}/subscribe")
    public CustomerSubscriptionDto subscribe(@PathVariable("productId") Long productId, @Valid @RequestBody SubscriptionRequestDto subscriptionRequest) {
        return productService.subscribe(productId, subscriptionRequest);
    }

}
