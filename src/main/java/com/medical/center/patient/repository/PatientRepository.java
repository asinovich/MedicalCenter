package com.medical.center.patient.repository;

import com.medical.center.employee.model.Employee;
import com.medical.center.patient.model.Patient;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findById(Long id);

    Optional<Patient> findByFirstNameAndLastName(String firstName, String lastName);

    List<Patient> findByDeletedAtIsNull();
}
