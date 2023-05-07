package com.medical.center.employee.repository;

import com.medical.center.employee.model.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findById(Long id);

    Optional<Employee> findByUserId(Long userId);

    List<Employee> findAllByDeletedAtIsNull();
}
