package com.radiantSKill.infiniti.repository;

import com.radiantSKill.infiniti.entity.SchoolPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

    public interface PrincipalRepository
            extends JpaRepository<SchoolPrincipal, Long> {

        Optional<SchoolPrincipal> findByPrincipalEmail(String email);
        Optional<SchoolPrincipal> findByAppUser_Id(Long appUserId);
    }

