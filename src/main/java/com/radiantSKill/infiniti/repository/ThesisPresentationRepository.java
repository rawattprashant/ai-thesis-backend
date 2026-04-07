package com.radiantSKill.infiniti.repository;

import com.radiantSKill.infiniti.entity.AppUser;
import com.radiantSKill.infiniti.entity.ThesisPresentation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThesisPresentationRepository extends JpaRepository<ThesisPresentation, Long> {
    Optional<ThesisPresentation> findByStudent(AppUser student);
    long count();
}
