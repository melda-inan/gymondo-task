package com.gymondo.service;

import com.gymondo.model.dto.CustomerSubscriptionDto;
import com.gymondo.model.dto.ProductDto;
import com.gymondo.model.dto.SubscriptionRequestDto;
import com.gymondo.model.entity.CustomerSubscription;
import com.gymondo.model.entity.Product;
import com.gymondo.model.mapper.ProductMapper;
import com.gymondo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CustomerSubscriptionService customerSubscriptionService;

    public ProductServiceImpl(ProductRepository productRepository, CustomerSubscriptionService customerSubscriptionService) {
        this.productRepository = productRepository;
        this.customerSubscriptionService = customerSubscriptionService;
    }

    @Override
    public List<ProductDto> listProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> listProductsWithVoucherCode(String voucherCode) {
        List<Product> products = productRepository.findByVoucherCode(voucherCode);
        return products.stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProduct(Long id) {
        Product product = getProductEntity(id);
        return ProductMapper.toDto(product);
    }

    private Product getProductEntity(Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new EntityNotFoundException("No product found with id: " + id);
        }

        return product.get();
    }

    @Override
    public CustomerSubscriptionDto subscribe(Long productId, SubscriptionRequestDto subscriptionRequest) {
        Product product = getProductEntity(productId);
        CustomerSubscription customerSubscription = customerSubscriptionService.generateCustomerSubscription(product, subscriptionRequest);
        return customerSubscriptionService.save(customerSubscription);
    }
}
