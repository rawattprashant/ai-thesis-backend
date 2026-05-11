package com.radiantSKill.infiniti.repository;

import com.radiantSKill.infiniti.entity.AppUser;
import com.radiantSKill.infiniti.entity.ThesisRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ThesisRegistrationRepository
        extends JpaRepository<ThesisRegistration, Long> {

    Optional<ThesisRegistration> findByStudent(AppUser student);

    boolean existsByStudent(AppUser student);
    long count();

    Long countByHasInvestorInterestTrue();

    @Query("SELECT COUNT(DISTINCT t.schoolName) FROM ThesisRegistration t")
    Long countDistinctSchoolName();

    Long countByHasDigitalPrototypeTrue();
}
