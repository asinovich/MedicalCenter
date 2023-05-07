package com.medical.center.user.service.impl;

import com.medical.center.base.exception.AuthorizationException;
import com.medical.center.employee.service.EmployeeService;
import com.medical.center.user.service.UserService;
import com.medical.center.user.model.User;
import com.medical.center.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public User create(User user) {
        log.info("Save user={}", user);
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        log.info("Update user={}", user);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        log.info("Soft delete user by id={}", id);
        User user = getById(id);

        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void hardDeleteByEmployeeId(Long employeeId) {
        log.info("Soft delete user by employeeId={}", employeeId);
        userRepository.deleteByEmployeeId(employeeId);
    }

    @Override
    public User getById(Long id) {
        log.info("Get user by id={}", id);
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getAll() {
        log.info("Get all users");
        return userRepository.findAllByDeletedAtIsNull();
    }

    @Override
    public List<User> getAllWithoutEmployee() {
        log.info("Get all users without employee");
        return userRepository.findByEmployeeIdIsNull();
    }

    @Override
    public void login(String email, String password) {
        log.info("Fetching user with email: [{}]", email);
        Optional<User> user = userRepository.findByEmailAndPassword(email, password);

        if (user.isEmpty()) {
            log.warn("User with email: [{}] not found", email);
            throw new AuthorizationException("Bad credentials");
        }
    }

    @Override
    public User getByEmail(String email) {
        log.info("Get user by email={}", email);
        return userRepository.findByEmail(email).orElse(null);
    }
}
