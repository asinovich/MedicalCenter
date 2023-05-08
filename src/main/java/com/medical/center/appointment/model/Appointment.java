package com.medical.center.appointment.model;

import com.medical.center.base.enums.AppointmentStatus;
import com.medical.center.base.enums.AppointmentType;
import com.medical.center.base.model.BaseEntity;
import com.medical.center.employee.model.Employee;
import com.medical.center.patient_record.model.PatientRecord;
import com.medical.center.room.model.Room;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointment")
public class Appointment extends BaseEntity<Long> {

    @Column(name = "visit_date_time")
    private LocalDateTime visitDateTime;

    @Column(name = "note")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type", nullable = false,
        columnDefinition = "enum('CONSULTATION', 'VISIT', 'SURGERY')")
    private AppointmentType appointmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",
        columnDefinition = "enum('ACTIVE', 'CANCELED', 'IN_A_ROOM', 'COMPLETED')")
    private AppointmentStatus status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "patient_record_id")
    private PatientRecord patientRecord;

    @OneToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public String getPatientFullName() {
        return patientRecord.getPatient().getFullName();
    }
}
