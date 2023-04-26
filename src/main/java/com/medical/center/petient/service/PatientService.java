package com.medical.center.petient.service;

import com.medical.center.employee.model.Employee;
import com.medical.center.petient.model.Patient;
import java.util.List;

public interface PatientService {

    Patient create(Patient patient);

    Patient update(Patient patient);

    void softDelete(Long id);

    void hardDelete(Long id);

    Patient getById(Long id);

    List<Patient> getAll();
}
