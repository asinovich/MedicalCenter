package com.medical.center.employee.service;

import com.medical.center.employee.model.Employee;
import java.util.List;

public interface EmployeeService {

    Employee save(Employee employee);

    Employee update(Employee employee);

    void delete(Long id);

    Employee getById(Long id);

    List<Employee> getAll();
}
