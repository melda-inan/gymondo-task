package com.gymondo.repository;

import com.gymondo.model.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Voucher findByCode(String code);
}
