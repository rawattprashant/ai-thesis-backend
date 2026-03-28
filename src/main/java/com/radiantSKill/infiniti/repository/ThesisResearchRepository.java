package com.radiantSKill.infiniti.repository;

import com.radiantSKill.infiniti.entity.ThesisResearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThesisResearchRepository extends JpaRepository<ThesisResearch, Long> {

    Optional<ThesisResearch> findByThesisId(Long thesisId);
}