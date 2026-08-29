package com.radiantSKill.infiniti.services;

import com.radiantSKill.infiniti.dao.AdminDashboardDAO;
import com.radiantSKill.infiniti.dto.*;
import com.radiantSKill.infiniti.entity.AppUser;
import com.radiantSKill.infiniti.entity.StudentSubmissionStore;
import com.radiantSKill.infiniti.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final AdminDashboardDAO adminDashboardDAO;

    private final AppUserRepository userRepository;

    private final ThesisRegistrationRepository thesisRegistrationRepository;
    private final ProofOfConceptRepository proofRepository;
    private final FinancialModelRepository financialRepository;
    private final ThesisPresentationRepository presentationRepository;
    private final SelfieVideoRepository selfieRepository;
    private final ThesisResearchRepository thesisResearchRepository;
    private final DigitalPrototypeRepository digitalPrototypeRepository;

    private final StudentSubmissionStoreRepository studentSubmissionStoreRepository;

    public DashboardStatsDTO getStats() {

        Long students = userRepository.count();

        Long schools = 0L;
        Long prototypes = 0L;
        Long investible = 0L;

        return new DashboardStatsDTO(
                students,
                schools,
                prototypes,
                investible
        );
    }

    public Page<StudentSubmissionStore> filter(AdminFilterRequest req) {

        Pageable pageable = PageRequest.of(
                req.getPage(),
                req.getSize(),
                Sort.by("createdAt").descending()
        );

        return studentSubmissionStoreRepository.findByOverallStatusContainingIgnoreCaseAndThesisTopicContainingIgnoreCaseAndGradeContainingIgnoreCaseAndSchoolNameContainingIgnoreCase(
                defaultVal(req.getStatus()),
                defaultVal(req.getTopic()),
                defaultVal(req.getGrade()),
                defaultVal(req.getSchoolName()),
                pageable
        );
    }

    private String defaultVal(String val) {
        return val == null ? "" : val;
    }

    private String resolveStage(AppUser student) {

        if (selfieRepository.findByStudent(student).isPresent()) {
            return "FINAL_SUBMISSION";
        }

        if (presentationRepository.findByStudent(student).isPresent()) {
            return "PRESENTATION";
        }

        if (financialRepository.findByStudent(student).isPresent()) {
            return "FINANCIAL_MODEL";
        }

        if (digitalPrototypeRepository.findByStudent(student).isPresent()) {
            return "DIGITAL_PROTOTYPE";
        }

        if (proofRepository.findByStudent(student).isPresent()) {
            return "PROOF_OF_CONCEPT";
        }

        if (thesisResearchRepository.findByStudent(student).isPresent()) {
            return "RESEARCH";
        }

        if (thesisRegistrationRepository.findByStudent(student).isPresent()) {
            return "REGISTRATION_COMPLETED";
        }

        return "NOT_STARTED";
    }

    public AdminDashboardSummaryDTO getSummary()    {

        AdminDashboardSummaryDTO dto = new AdminDashboardSummaryDTO();

        dto.setTotalStudents(userRepository.countByRoles_Name("STUDENT"));
        dto.setTotalRegistrations(thesisRegistrationRepository.count());
        dto.setPocCompleted(proofRepository.count());
        dto.setFinancialModelCompleted(financialRepository.count());
        dto.setPresentationsCompleted(presentationRepository.count());
        dto.setFinalSubmissions(selfieRepository.count());

        return dto;
    }

    public List<StudentDashboardDTO> getStudents(
            DashboardFilterRequest filter,
            int page,
            int size
    ) {

        return adminDashboardDAO.getStudents(filter,page,size);

    }

    public List<AdminStudentDTO> getStudents() {

        List<AppUser> students = userRepository.findAll(); // filter if needed

        return students.stream().map(user -> {
            AdminStudentDTO dto = new AdminStudentDTO();
            dto.setName(user.getFirstName() + " " + user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setStatus(user.getStatus());

            dto.setCurrentStage("REGISTERED"); // You can enhance later

            return dto;
        }).toList();
    }

    public List<StudentSubmissionStore> getAllSubmissions(
            DashboardFilterRequest filter,
            int page,
            int size
    ) {
        return adminDashboardDAO.getAllSubmissions(filter, page, size);
    }

}