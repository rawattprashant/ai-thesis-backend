package com.radiantSKill.infiniti.repository;

import com.radiantSKill.infiniti.entity.AppUser;
import com.radiantSKill.infiniti.entity.SelfieVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SelfieVideoRepository extends JpaRepository<SelfieVideo, Long> {
    Optional<SelfieVideo> findByStudent(AppUser student);
}
