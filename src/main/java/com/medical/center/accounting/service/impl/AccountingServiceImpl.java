package com.medical.center.accounting.service.impl;

import com.medical.center.accounting.model.Accounting;
import com.medical.center.accounting.repository.AccountingRepository;
import com.medical.center.accounting.service.AccountingService;
import com.medical.center.invoice.service.InvoiceService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountingServiceImpl implements AccountingService {

    @Autowired
    private AccountingRepository accountingRepository;

    @Override
    public Accounting create(Accounting accounting) {
        log.info("Save accounting={}", accounting);
        return accountingRepository.save(accounting);
    }

    @Override
    public Accounting update(Accounting accounting) {
        log.info("Update accounting={}", accounting);
        return accountingRepository.save(accounting);
    }

    @Override
    public void delete(Long id) {
        log.info("Delete accounting by id={}", id);
        accountingRepository.delete(id);
    }

    @Override
    public Accounting getById(Long id) {
        log.info("Get accounting by id={}", id);
        return accountingRepository.findById(id).orElse(null);
    }

    @Override
    public List<Accounting> getAll() {
        log.info("Get all accountings");
        return accountingRepository.findAll();
    }
}
