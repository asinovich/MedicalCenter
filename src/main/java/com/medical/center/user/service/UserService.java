package com.medical.center.user.service;

import com.medical.center.employee.model.Employee;
import com.medical.center.user.model.User;
import java.util.List;

public interface UserService {

    User create(User user);

    User update(User user);

    void softDelete(Long id);

    void hardDeleteByEmployeeId(Long employeeId);

    User getById(Long id);

    List<User> getAll();

    List<User> getAllWithoutEmployee();

    void login (String email, String password);

    User getByEmail(String email);
}
