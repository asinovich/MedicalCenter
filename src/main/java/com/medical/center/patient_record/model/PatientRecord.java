package com.medical.center.patient_record.model;

import com.medical.center.appointment.model.Appointment;
import com.medical.center.base.model.BaseEntity;
import com.medical.center.patient.model.Patient;
import com.medical.center.treatment_outcomes.model.TreatmentOutcomes;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "patient_record")
public class PatientRecord extends BaseEntity<Long> {

/*    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @WhereJoinTable(clause = "deleted_at IS NULL or now() < deleted_at")
    private EmployeeProvider provider;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;*/

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany
    @JoinColumn(name = "treatment_outcomes_id")
    private List<TreatmentOutcomes> treatmentOutcomes;

    @OneToMany
    @JoinColumn(name = "appointment_id")
    private List<Appointment> appointments;

/*    @OrderBy("createdAt, updatedAt ASC")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "patient_record_file",
        joinColumns = @JoinColumn(name = "patient_record_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "file_id", referencedColumnName = "id"))
    private List<File> files;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "photo_id")
    private File photo;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patientRecord")
    @OrderBy("visitDateTime DESC")
    private List<Appointment> appointments;

    @OneToOne(mappedBy = "patientRecord")
    private Insurance insurance;

    @OneToOne(mappedBy = "patientRecord")
    private Demographics demographics;

    @OneToMany(mappedBy = "patientRecord")
    private List<LabResult> labResults;*/
}
