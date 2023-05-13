package com.medical.center.treatment_outcomes.model;

import com.medical.center.base.model.BaseEntity;
import com.medical.center.employee.model.Employee;
import com.medical.center.patient.model.Patient;
import com.medical.center.patient_record.model.PatientRecord;
import java.time.LocalDate;
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
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "treatment_outcomes")
public class TreatmentOutcomes extends BaseEntity<Long> {

    @OneToOne
    @JoinColumn(name = "patient_record_id")
    private PatientRecord patientRecord;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "diagnosis", nullable = false, length = 1000)
    private String diagnosis;

    @Column(name = "treatment", nullable = false, length = 1000)
    private String treatment;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    public String getPatientFullName() {
        return patientRecord.getPatient().getFullName();
    }
}
