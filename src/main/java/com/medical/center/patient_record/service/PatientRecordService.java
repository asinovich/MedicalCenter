package com.medical.center.patient_record.service;

import com.medical.center.patient_record.model.PatientRecord;
import java.util.List;

public interface PatientRecordService {

    PatientRecord create(PatientRecord patientRecord);

    PatientRecord update(PatientRecord patientRecord);

    void softDelete(Long id);

    void hardDelete(Long id);

    PatientRecord getById(Long id);

    PatientRecord getByPatientId(Long patientId);

    List<PatientRecord> getAll();
}
