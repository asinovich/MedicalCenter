package com.medical.center.treatment_outcomes.service;

import com.medical.center.treatment_outcomes.model.TreatmentOutcomes;
import java.util.List;

public interface TreatmentOutcomesService {

    TreatmentOutcomes create(TreatmentOutcomes treatmentOutcomes);

    TreatmentOutcomes update(TreatmentOutcomes treatmentOutcomes);

    TreatmentOutcomes getById(Long id);

    void delete(Long id);

    List<TreatmentOutcomes> getByPatientId(Long patientId);

    List<TreatmentOutcomes> getByEmployeeId(Long employeeId);

    List<TreatmentOutcomes> getAll();
}
