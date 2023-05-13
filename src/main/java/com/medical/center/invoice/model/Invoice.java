package com.medical.center.invoice.model;

import com.medical.center.accounting.model.Accounting;
import com.medical.center.base.model.BaseEntity;
import com.medical.center.employee.model.Employee;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoice")
public class Invoice extends BaseEntity<Long> {

    @Column(name = "total_coast")
    private BigDecimal totalCoast;

    @Column(name = "note", length = 1000)
    private String note;

    @OneToOne
    @JoinColumn(name = "accounting_id")
    private Accounting accounting;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
