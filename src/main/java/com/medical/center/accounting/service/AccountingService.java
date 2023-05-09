package com.medical.center.accounting.service;

import com.medical.center.accounting.model.Accounting;
import java.util.List;

public interface AccountingService {

    Accounting create(Accounting accounting);

    Accounting update(Accounting accounting);

    void delete(Long id);

    Accounting getById(Long id);

    List<Accounting> getAll();
}
