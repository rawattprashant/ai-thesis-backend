package com.radiantSKill.infiniti.repository;

import com.radiantSKill.infiniti.entity.AppUser;
import com.radiantSKill.infiniti.entity.ThesisRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThesisRegistrationRepository
        extends JpaRepository<ThesisRegistration, Long> {

    Optional<ThesisRegistration> findByStudent(AppUser student);

    boolean existsByStudent(AppUser student);
    long count();
}
