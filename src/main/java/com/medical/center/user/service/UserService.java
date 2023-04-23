package com.medical.center.user.service;

import com.medical.center.user.model.User;
import java.util.List;

public interface UserService {

    User save(User user);

    User update(User user);

    void delete(Long id);

    User getById(Long id);

    List<User> getAll();

    void login (String email, String password);
}
