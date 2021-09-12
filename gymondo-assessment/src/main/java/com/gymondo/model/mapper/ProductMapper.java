package com.gymondo.model.mapper;

import com.gymondo.model.dto.ProductDto;
import com.gymondo.model.dto.SubscriptionDto;
import com.gymondo.model.dto.VoucherDto;
import com.gymondo.model.entity.Product;
import com.gymondo.model.entity.Voucher;

import java.util.List;
import java.util.stream.Collectors;

public final class ProductMapper {

    public static Product fromId(Long id) {
        if (id == null) {
            return null;
        }

        Product product = new Product();
        product.setId(id);
        return product;
    }

    public static Product toEntity(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }

        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());

        if (productDto.getVouchers() != null) {
            List<Voucher> vouchers = productDto.getVouchers()
                    .stream()
                    .map(VoucherMapper::toEntity)
                    .collect(Collectors.toList());

            product.setVouchers(vouchers);
        }

        return product;
    }

    public static ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());

        if (product.getVouchers() != null) {
            List<VoucherDto> vouchers = product.getVouchers()
                    .stream()
                    .map(VoucherMapper::toDto)
                    .collect(Collectors.toList());

            productDto.setVouchers(vouchers);
        }

        if (product.getSubscriptions() != null) {
            List<SubscriptionDto> subscriptions = product.getSubscriptions()
                    .stream()
                    .peek(it -> it.setProduct(null))
                    .map(SubscriptionMapper::toDto)
                    .collect(Collectors.toList());

            productDto.setSubscriptions(subscriptions);
        }

        return productDto;
    }
}
