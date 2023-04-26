package com.medical.center.patient_record.service.impl;

import com.medical.center.patient_record.model.PatientRecord;
import com.medical.center.patient_record.repository.PatientRecordRepository;
import com.medical.center.patient_record.service.PatientRecordService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PatientRecordServiceImpl implements PatientRecordService {

    private final PatientRecordRepository patientRecordRepository;

    @Override
    public PatientRecord create(PatientRecord patientRecord) {
        log.info("Save patientRecord={}", patientRecord);
        return patientRecordRepository.save(patientRecord);
    }

    @Override
    public PatientRecord update(PatientRecord patientRecord) {
        log.info("Update patientRecord={}", patientRecord);
        return patientRecordRepository.save(patientRecord);
    }

    @Override
    public void softDelete(Long id) {
        log.info("Soft delete patientRecord by id={}", id);
        PatientRecord patientRecord = getById(id);

        patientRecord.setDeletedAt(LocalDateTime.now());

        patientRecordRepository.save(patientRecord);
    }

    @Override
    public void hardDelete(Long id) {
        log.info("Hard delete patientRecord by id={}", id);
        patientRecordRepository.delete(id);
    }

    @Override
    public PatientRecord getById(Long id) {
        log.info("Get patientRecord by id={}", id);
        return patientRecordRepository.findById(id).orElse(null);
    }

    @Override
    public List<PatientRecord> getAll() {
        log.info("Get all patientRecords");
        return patientRecordRepository.findAll();
    }
}
