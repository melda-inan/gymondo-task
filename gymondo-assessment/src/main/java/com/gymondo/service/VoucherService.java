package com.gymondo.service;

import com.gymondo.model.dto.VoucherDto;
import com.gymondo.model.entity.Voucher;

import java.util.List;

public interface VoucherService {

    List<VoucherDto> listVouchers();
    Voucher getVoucherEntity(Long id);
    Voucher getVoucherEntityByCode(String code);
    VoucherDto getVoucher(Long id);
    VoucherDto getVoucherByCode(String code);
    Boolean validateVoucher(String code);
}
