package com.radiantSKill.infiniti.repository;

import com.radiantSKill.infiniti.entity.AppUser;
import com.radiantSKill.infiniti.entity.StudentSubmissionStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentSubmissionStoreRepository extends JpaRepository<StudentSubmissionStore, Long> {

    Optional<StudentSubmissionStore> findByStudent(AppUser student);

    Optional<StudentSubmissionStore> findByStudentId(Long studentId);

    List<StudentSubmissionStore> findByOverallStatus(String status);

    List<StudentSubmissionStore> findByThesisTopic(String topic);

    List<StudentSubmissionStore> findByGrade(String grade);

    List<StudentSubmissionStore> findBySchoolName(String schoolName);

    List<StudentSubmissionStore> findByRegistrationStatus(String status);

    List<StudentSubmissionStore> findByResearchStatus(String status);

    List<StudentSubmissionStore> findByPocStatus(String status);

    List<StudentSubmissionStore> findByFinancialModelStatus(String status);

    List<StudentSubmissionStore> findByPresentationStatus(String status);

    List<StudentSubmissionStore> findBySelfieVideoStatus(String status);

    Page<StudentSubmissionStore> findByOverallStatusContainingIgnoreCaseAndThesisTopicContainingIgnoreCaseAndGradeContainingIgnoreCaseAndSchoolNameContainingIgnoreCase(
            String status,
            String topic,
            String grade,
            String schoolName,
            Pageable pageable
    );
}