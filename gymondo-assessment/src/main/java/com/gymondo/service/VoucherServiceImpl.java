package com.gymondo.service;

import com.gymondo.model.dto.VoucherDto;
import com.gymondo.model.entity.Voucher;
import com.gymondo.model.mapper.VoucherMapper;
import com.gymondo.repository.VoucherRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    public VoucherServiceImpl(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @Override
    public List<VoucherDto> listVouchers() {
        List<Voucher> vouchers = voucherRepository.findAll();
        return vouchers.stream()
                .map(VoucherMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Voucher getVoucherEntity(Long id) {
        Optional<Voucher> voucher = voucherRepository.findById(id);

        if (voucher.isEmpty()) {
            throw new EntityNotFoundException("No voucher found with id: " + id);
        }

        return voucher.get();
    }

    @Override
    public Voucher getVoucherEntityByCode(String code) {
        Voucher voucher = voucherRepository.findByCode(code);

        if (voucher == null) {
            throw new EntityNotFoundException("No voucher found with code: " + code);
        }

        return voucher;
    }

    @Override
    public VoucherDto getVoucher(Long id) {
        Voucher voucher = getVoucherEntity(id);

        return VoucherMapper.toDto(voucher);
    }

    @Override
    public VoucherDto getVoucherByCode(String code) {
        Voucher voucher = voucherRepository.findByCode(code);

        if (voucher == null) {
            throw new EntityNotFoundException("No voucher found with code: " + code);
        }

        return VoucherMapper.toDto(voucher);
    }

    @Override
    public Boolean validateVoucher(String code) {
        return voucherRepository.findByCode(code) != null;
    }

}
