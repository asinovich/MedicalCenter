package com.medical.center.treatment_outcomes.repository;

import com.medical.center.treatment_outcomes.model.TreatmentOutcomes;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentOutcomesRepository extends JpaRepository<TreatmentOutcomes, Long> {

    Optional<TreatmentOutcomes> findById(Long id);

    List<TreatmentOutcomes> findByPatientId(Long patientId);

    List<TreatmentOutcomes> findByEmployeeId(Long employeeId);

    List<TreatmentOutcomes> findAllByDeletedAtIsNull();
}
