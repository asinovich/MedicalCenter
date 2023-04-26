package com.medical.center.employee.service.impl;

import com.medical.center.employee.model.Employee;
import com.medical.center.employee.repository.EmployeeRepository;
import com.medical.center.employee.service.EmployeeService;
import com.medical.center.user.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserService userService;

    @Override
    public Employee create(Employee employee) {
        log.info("Save employee={}", employee);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        log.info("Update employee={}", employee);
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        log.info("Soft delete employee by id={}", id);
        Employee employee = getById(id);

        userService.softDelete(employee.getUser().getId());

        employee.setDeletedAt(LocalDateTime.now());

        employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void hardDelete(Long id) {
        log.info("Hard delete employee by id={}", id);
        Employee employee = getById(id);

        userService.hardDelete(employee.getUser().getId());

        employeeRepository.delete(employee);
    }

    @Override
    public Employee getById(Long id) {
        log.info("Get employee by id={}", id);
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Employee> getAll() {
        log.info("Get all employees");
        return employeeRepository.findAll();
    }
}
