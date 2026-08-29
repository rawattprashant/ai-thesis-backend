package com.radiantSKill.infiniti.repository;

import com.radiantSKill.infiniti.entity.AppUser;
import com.radiantSKill.infiniti.entity.DigitalPrototype;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DigitalPrototypeRepository extends JpaRepository<DigitalPrototype, Long> {
    Optional<DigitalPrototype> findByStudent(AppUser student);
}
