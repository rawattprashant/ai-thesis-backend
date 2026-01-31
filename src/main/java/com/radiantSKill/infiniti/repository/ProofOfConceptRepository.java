package com.radiantSKill.infiniti.repository;

import com.radiantSKill.infiniti.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProofOfConceptRepository extends JpaRepository<ProofOfConcept, Long> {
    Optional<ProofOfConcept> findByStudent(AppUser student);
}
