package com.radiantSKill.infiniti.repository;

import com.radiantSKill.infiniti.entity.AppUser;
import com.radiantSKill.infiniti.entity.FinancialModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinancialModelRepository extends JpaRepository<FinancialModel, Long> {
    Optional<FinancialModel> findByStudent(AppUser student);
    long count();
}
