package com.medical.center.appointment.repository;

import com.medical.center.appointment.model.Appointment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findById(Long id);

    List<Appointment> findByPatientRecordId(Long patientRecordId);
}
