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

        Long students = userRepository.countByRoles_Name("STUDENT");

        Long schools = thesisRegistrationRepository.countDistinctSchoolName();

        Long prototypes = thesisRegistrationRepository.countByHasDigitalPrototypeTrue();

        Long investible = thesisRegistrationRepository.countByHasInvestorInterestTrue();

        return new DashboardStatsDTO(
                students,
                schools,
                prototypes,
                investible
        );
    }

    public AdminDashboardSummaryDTO getSummary() {

        return AdminDashboardSummaryDTO.builder()
                .totalStudents(userRepository.countByRoles_Name("STUDENT"))
                .totalRegistrations(thesisRegistrationRepository.count())
                .pocCompleted(proofRepository.count())
                .financialModelCompleted(financialRepository.count())
                .presentationsCompleted(presentationRepository.count())
                .finalSubmissions(selfieRepository.count())
                .build();
    }

    public List<AdminStudentDTO> getAllStudents(int page, int size) {
        return adminDashboardDAO.getAllStudents(page, size);
    }

    public List<AdminStudentDTO> getStudentsLight(
            DashboardFilterRequest filter,
            int page,
            int size
    ) {
        return adminDashboardDAO.getStudents(filter, page, size);
    }

}