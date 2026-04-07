package com.radiantSKill.infiniti.repository;

import com.radiantSKill.infiniti.entity.AppUser;
import com.radiantSKill.infiniti.entity.ThesisResearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ThesisResearchRepository extends JpaRepository<ThesisResearch, Long> {

    Optional<ThesisResearch> findByThesisId(Long thesisId);
    Optional<ThesisResearch> findByStudent(AppUser student);
    @Query("SELECT COALESCE(MAX(t.id),0) FROM ThesisTopic t")
    Long getMaxId();
}