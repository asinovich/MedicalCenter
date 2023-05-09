package com.medical.center.invoice.service.impl;

import com.medical.center.invoice.model.Invoice;
import com.medical.center.invoice.repository.InvoiceRepository;
import com.medical.center.invoice.service.InvoiceService;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public Invoice create(Invoice invoice) {
        log.info("Save invoice={}", invoice);
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice update(Invoice invoice) {
        log.info("Update invoice={}", invoice);
        return invoiceRepository.save(invoice);
    }

    @Override
    public void delete(Long id) {
        log.info("Delete invoice by id={}", id);
        invoiceRepository.delete(id);
    }

    @Override
    public Invoice getById(Long id) {
        log.info("Get invoice by id={}", id);
        return invoiceRepository.findById(id).orElse(null);
    }

    @Override
    public List<Invoice> getAll() {
        log.info("Get all invoices");
        return invoiceRepository.findAll();
    }

    @Override
    public List<Invoice> getByAccountingId(Long accountingId) {
        log.info("Get invoices by accountingId={}", accountingId);
        return invoiceRepository.findByAccountingId(accountingId);
    }

    @Override
    public BigDecimal calculateTotalIncomeByAccounting(Long accountingId) {
        log.info("Calculate total income by accounting={}", accountingId);
        List<Invoice> invoices = invoiceRepository.findByAccountingId(accountingId);
        List<BigDecimal> bigDecimalList = invoices.stream()
            .map(Invoice::getTotalCoast)
            .collect(Collectors.toList());

        BigDecimal totalIncome = new BigDecimal(0);

        for (var item: bigDecimalList) {
            totalIncome = totalIncome.add(item);
        }

        return totalIncome;
    }
}
