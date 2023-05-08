package com.medical.center.patient.service.impl;

import com.medical.center.patient.model.Patient;
import com.medical.center.patient.repository.PatientRepository;
import com.medical.center.patient.service.PatientService;
import com.medical.center.patient_record.model.PatientRecord;
import com.medical.center.patient_record.service.PatientRecordService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientRecordService patientRecordService;

    @Override
    @Transactional
    public Patient create(Patient patient) {
        log.info("Save patient={}", patient);
        Patient createdPatient = patientRepository.save(patient);

        createPatientRecord(patient);

        return createdPatient;
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

        patientRecordService.softDelete(patient.getPatientRecords().getId());

        patient.setDeletedAt(LocalDateTime.now());

        patientRepository.save(patient);
    }

    @Override
    @Transactional
    public void hardDelete(Long id) {
        log.info("Hard delete patient by id={}", id);
        Patient patient = getById(id);

        patientRecordService.hardDelete(patient.getPatientRecords().getId());

        patientRepository.delete(patient);
    }

    @Override
    public Patient getById(Long id) {
        log.info("Get patient by id={}", id);
        return patientRepository.findById(id).orElse(null);
    }

    @Override
    public Patient getByFullName(String fullName) {
        log.info("Get patient by fullName={}", fullName);
        String[] substrings = fullName.split(" ");
        return patientRepository.findByFirstNameAndLastName(substrings[1], substrings[0]).orElse(null);
    }

    @Override
    public List<Patient> getAll() {
        log.info("Get all patients");
        return patientRepository.findByDeletedAtIsNull();
    }

    private void createPatientRecord(Patient patient) {
        PatientRecord patientRecord = PatientRecord.builder()
            .patient(patient)
            .build();

        patientRecordService.create(patientRecord);
    }
}
