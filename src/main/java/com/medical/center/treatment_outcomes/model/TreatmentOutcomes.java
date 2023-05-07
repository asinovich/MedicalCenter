package com.medical.center.treatment_outcomes.model;

import com.medical.center.base.model.BaseEntity;
import com.medical.center.employee.model.Employee;
import com.medical.center.petient.model.Patient;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "treatment_outcomes")
public class TreatmentOutcomes extends BaseEntity<Long> {

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "diagnosis", nullable = false)
    private String diagnosis;

    @Column(name = "treatment", nullable = false)
    private String treatment;

    @Column(name = "date", nullable = false)
    private LocalDate date;
}
