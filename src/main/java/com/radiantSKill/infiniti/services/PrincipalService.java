package com.radiantSKill.infiniti.services;


import com.radiantSKill.infiniti.dao.PrincipalDashboardDAO;
import com.radiantSKill.infiniti.dto.*;
import com.radiantSKill.infiniti.entity.SchoolPrincipal;
import com.radiantSKill.infiniti.repository.PrincipalRepository;
import com.radiantSKill.infiniti.repository.ThesisRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
    public class PrincipalService {


    private final  PrincipalRepository repository;
    private final ThesisRegistrationRepository thesisRegistrationRepository;
    private final PrincipalDashboardDAO principalDashboardDAO;


    // Register School (First Time Only)
        public PrincipalResponse registerSchool(
                PrincipalRequestDTO dto) {

            if (repository.findByPrincipalEmail(
                    dto.getPrincipalEmail()).isPresent()) {

                throw new RuntimeException(
                        "School already registered for this principal");
            }

            SchoolPrincipal school = new SchoolPrincipal();

            school.setPrincipalName(dto.getPrincipalName());
            school.setSchoolName(dto.getSchoolName());
            school.setSchoolAddress(dto.getSchoolAddress());
            school.setPrincipalEmail(dto.getPrincipalEmail());

            SchoolPrincipal savedSchool = repository.save(school);

            PrincipalResponse response =
                    new PrincipalResponse();

            response.setId(savedSchool.getId());
            response.setPrincipalName(savedSchool.getPrincipalName());
            response.setSchoolName(savedSchool.getSchoolName());
            response.setSchoolAddress(savedSchool.getSchoolAddress());
            response.setPrincipalEmail(savedSchool.getPrincipalEmail());

            return response;
        }

        // Get Principal School Details
        public PrincipalResponse getSchool(Long id) {

            SchoolPrincipal school = repository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("School not found"));

            PrincipalResponse response =
                    new PrincipalResponse();

            response.setId(school.getId());
            response.setPrincipalName(school.getPrincipalName());
            response.setSchoolName(school.getSchoolName());
            response.setSchoolAddress(school.getSchoolAddress());
            response.setPrincipalEmail(school.getPrincipalEmail());
            response.setRegistered(true);

            return response;
        }

        public PrincipalStatsDTO getStats(String schoolName){
            Long students =
                    thesisRegistrationRepository
                            .countBySchoolName(schoolName);

            Long prototypes =
                    thesisRegistrationRepository
                            .countBySchoolNameAndHasDigitalPrototypeTrue(
                                    schoolName
                            );

            Long investible =
                    thesisRegistrationRepository
                            .countBySchoolNameAndHasInvestorInterestTrue(
                                    schoolName
                            );

            return new PrincipalStatsDTO(
                    students,
                    1L,
                    prototypes,
                    investible
            );

        }
    public String getSchoolNameByEmail(String email) {

        SchoolPrincipal school =
                repository
                        .findByPrincipalEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "School not found"
                                ));

        return school.getSchoolName();
    }
    public List<PrincipalStudentDTO> getAllStudents(String schoolName,int page, int size) {
        return principalDashboardDAO.getStudentsBySchool(schoolName,page, size);
    }
    public List<PrincipalStudentDTO> getStudentsLight(
            PrincipalDashboardFilterRequest filter,
            String schoolName,
            int page,
            int size
    ) {
        return principalDashboardDAO.getStudents(schoolName,filter, page, size);
    }
    }

