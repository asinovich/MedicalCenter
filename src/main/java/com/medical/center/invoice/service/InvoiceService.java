package com.medical.center.invoice.service;

import com.medical.center.invoice.model.Invoice;
import java.math.BigDecimal;
import java.util.List;

public interface InvoiceService {

    Invoice create(Invoice invoice);

    Invoice update(Invoice invoice);

    void delete(Long id);

    Invoice getById(Long id);

    List<Invoice> getAll();

    BigDecimal calculateTotalIncomeByAccounting(Long accountingId);
}
