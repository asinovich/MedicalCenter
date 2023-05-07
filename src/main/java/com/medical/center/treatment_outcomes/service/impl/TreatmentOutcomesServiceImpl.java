package com.medical.center.treatment_outcomes.service.impl;

import com.medical.center.treatment_outcomes.model.TreatmentOutcomes;
import com.medical.center.treatment_outcomes.repository.TreatmentOutcomesRepository;
import com.medical.center.treatment_outcomes.service.TreatmentOutcomesService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TreatmentOutcomesServiceImpl implements TreatmentOutcomesService {

    @Autowired
    private TreatmentOutcomesRepository treatmentOutcomesRepository;

    @Override
    public TreatmentOutcomes create(TreatmentOutcomes treatmentOutcomes) {
        log.info("Save treatmentOutcomes={}", treatmentOutcomes);
        return treatmentOutcomesRepository.save(treatmentOutcomes);
    }

    @Override
    public TreatmentOutcomes update(TreatmentOutcomes treatmentOutcomes) {
        log.info("Update treatmentOutcomes={}", treatmentOutcomes);
        return treatmentOutcomesRepository.save(treatmentOutcomes);
    }

    @Override
    public TreatmentOutcomes getById(Long id) {
        log.info("Get treatmentOutcomes by id={}", id);
        return treatmentOutcomesRepository.findById(id).orElse(null);
    }

    @Override
    public List<TreatmentOutcomes> getByPatientId(Long patientId) {
        log.info("Get treatmentOutcomes by patientId={}", patientId);
        return treatmentOutcomesRepository.findByPatientId(patientId);
    }

    @Override
    public List<TreatmentOutcomes> getByEmployeeId(Long employeeId) {
        log.info("Get treatmentOutcomes by employeeId={}", employeeId);
        return treatmentOutcomesRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<TreatmentOutcomes> getAll() {
        log.info("Get all treatmentOutcomes");
        return treatmentOutcomesRepository.findAllByDeletedAtIsNull();
    }
}
