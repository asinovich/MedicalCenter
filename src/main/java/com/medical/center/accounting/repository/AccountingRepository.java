package com.medical.center.accounting.repository;

import com.medical.center.accounting.model.Accounting;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountingRepository extends JpaRepository<Accounting, Long> {

    Optional<Accounting> findById(Long id);
}
