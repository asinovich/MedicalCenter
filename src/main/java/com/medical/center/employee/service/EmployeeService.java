package com.medical.center.employee.service;

import com.medical.center.employee.model.Employee;
import java.util.List;

public interface EmployeeService {

    Employee create(Employee employee);

    Employee update(Employee employee);

    void softDelete(Long id);

    void hardDelete(Long id);

    Employee getById(Long id);

    Employee getByUserId(Long userId);

    Employee getByFullName(String fullName);

    List<Employee> getAll();
}
