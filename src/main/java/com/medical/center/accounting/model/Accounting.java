package com.medical.center.accounting.model;

import com.medical.center.base.model.BaseEntity;
import com.medical.center.invoice.model.Invoice;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Table(name = "accounting")
public class Accounting extends BaseEntity<Long> {

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 1000)

    private String description;

    @OneToMany
    @JoinColumn(name = "invoice_id")
    private List<Invoice> invoices;
}
