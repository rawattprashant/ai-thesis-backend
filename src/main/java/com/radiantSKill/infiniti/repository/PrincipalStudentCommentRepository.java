package com.radiantSKill.infiniti.repository;

import com.radiantSKill.infiniti.entity.PrincipalStudentComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrincipalStudentCommentRepository
        extends JpaRepository<PrincipalStudentComment, Long> {

    Optional<PrincipalStudentComment> findByThesisRegistration_Id(Long studentId);
}