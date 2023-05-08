package com.medical.center.employee.service.impl;

import com.medical.center.employee.model.Employee;
import com.medical.center.employee.repository.EmployeeRepository;
import com.medical.center.employee.service.EmployeeService;
import com.medical.center.user.model.User;
import com.medical.center.user.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Employee create(Employee employee) {
        log.info("Save employee={}", employee);

        Employee newEmployee =  employeeRepository.save(employee);

        updateUser(newEmployee);

        return newEmployee;
    }

    @Override
    public Employee update(Employee employee) {
        log.info("Update employee={}", employee);
        employee.setUpdatedAt(LocalDateTime.now());
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

        userService.hardDeleteByEmployeeId(id);
        employeeRepository.delete(id);
    }

    @Override
    public Employee getById(Long id) {
        log.info("Get employee by id={}", id);
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee getByFullName(String fullName) {
        log.info("Get employee by fullName={}", fullName);
        String[] substrings = fullName.split(" ");
        return employeeRepository.findByFirstNameAndLastName(substrings[1], substrings[0]).orElse(null);
    }

    @Override
    public List<Employee> getAll() {
        log.info("Get all employees");
        return employeeRepository.findAllByDeletedAtIsNull();
    }

    private void updateUser(Employee employee) {
        User user = employee.getUser();
        user.setEmployee(employee);
        userService.update(user);
    }
}
