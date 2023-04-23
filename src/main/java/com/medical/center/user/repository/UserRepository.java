package com.medical.center.user.repository;

import com.medical.center.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
                
    Optional<User> findById(Long id);

    Optional<User> findByEmailAndPassword(String email, String password);
}
