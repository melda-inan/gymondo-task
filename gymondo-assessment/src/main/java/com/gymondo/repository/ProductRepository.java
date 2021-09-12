package com.gymondo.repository;

import com.gymondo.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p.* " +
            "FROM product p, " +
                 "voucher v, " +
                 "product_voucher pv " +
            "WHERE p.id = pv. product_id " +
            "AND v.id = pv.voucher_id " +
            "AND v.code = :voucherCode",
            nativeQuery = true)
    List<Product> findByVoucherCode(@Param("voucherCode") String voucherCode);
}
