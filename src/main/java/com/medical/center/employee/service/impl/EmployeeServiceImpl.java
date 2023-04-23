package com.medical.center.employee.service.impl;

import com.medical.center.employee.model.Employee;
import com.medical.center.employee.repository.EmployeeRepository;
import com.medical.center.employee.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee getById(Long id) {
        log.info("Get employee by id={}", id);
        return employeeRepository.findById(id).orElse(null);
    }
}
