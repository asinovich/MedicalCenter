package com.medical.center.petient.service.impl;

import com.medical.center.petient.model.Patient;
import com.medical.center.petient.repository.PatientRepository;
import com.medical.center.petient.service.PatientService;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Patient create(Patient patient) {
        log.info("Save patient={}", patient);
        return patientRepository.save(patient);
    }

    @Override
    public Patient update(Patient patient) {
        log.info("Update patient={}", patient);
        return patientRepository.save(patient);
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        log.info("Soft delete patient by id={}", id);
        Patient patient = getById(id);

        //patientRecordRepository.softDelete(patient.getPatient().getId());

        patient.setDeletedAt(LocalDateTime.now());

        patientRepository.save(patient);
    }

    @Override
    @Transactional
    public void hardDelete(Long id) {
        log.info("Hard delete patient by id={}", id);
        Patient patient = getById(id);

        //patientRecordRepository.hardDelete(patient.getPatient().getId());

        patientRepository.delete(patient);
    }

    @Override
    public Patient getById(Long id) {
        log.info("Get patient by id={}", id);
        return patientRepository.findById(id).orElse(null);
    }

    @Override
    public List<Patient> getAll() {
        log.info("Get all patients");
        return patientRepository.findAll();
    }
}
