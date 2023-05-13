package com.medical.center.appointment.repository;

import com.medical.center.appointment.model.Appointment;
import com.medical.center.base.enums.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findById(Long id);

    List<Appointment> findByEmployeeIdAndStatusIsNot(Long employeeId, AppointmentStatus status);

    List<Appointment> findByPatientRecordId(Long patientRecordId);

    List<Appointment> findByEmployeeId(Long employeeId);
}
