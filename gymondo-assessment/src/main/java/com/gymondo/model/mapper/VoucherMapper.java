package com.gymondo.model.mapper;

import com.gymondo.model.dto.VoucherDto;
import com.gymondo.model.entity.Voucher;
import com.gymondo.model.enums.DiscountType;

public final class VoucherMapper {

    public static Voucher toEntity(VoucherDto voucherDto) {
        if (voucherDto == null) {
            return null;
        }

        Voucher voucher = new Voucher();
        voucher.setId(voucherDto.getId());
        voucher.setCode(voucherDto.getCode());
        voucher.setDiscountAmount(voucherDto.getDiscountAmount());
        voucher.setDiscountPercentage(voucherDto.getDiscountPercentage());
        voucher.setDiscountType(voucherDto.getDiscountType().getValue());
        return voucher;
    }

    public static VoucherDto toDto(Voucher voucher) {
        if(voucher == null) {
            return null;
        }

        VoucherDto voucherDto = new VoucherDto();
        voucherDto.setId(voucher.getId());
        voucherDto.setCode(voucher.getCode());
        voucherDto.setDiscountAmount(voucher.getDiscountAmount());
        voucherDto.setDiscountPercentage(voucher.getDiscountPercentage());
        voucherDto.setDiscountType(DiscountType.fromValue(voucher.getDiscountType()));
        return voucherDto;

    }
}
