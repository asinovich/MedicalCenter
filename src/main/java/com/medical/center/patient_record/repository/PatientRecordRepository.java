package com.medical.center.patient_record.repository;

import com.medical.center.patient_record.model.PatientRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRecordRepository extends JpaRepository<PatientRecord, Long> {

    Optional<PatientRecord> findById(Long id);

    Optional<PatientRecord> findByPatientId(Long patientId);

    List<PatientRecord> findByDeletedAtIsNull();
}
