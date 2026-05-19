package com.radiantSKill.infiniti.services;


import com.radiantSKill.infiniti.dto.PrincipalRequestDTO;
import com.radiantSKill.infiniti.dto.PrincipalResponse;
import com.radiantSKill.infiniti.entity.SchoolPrincipal;
import com.radiantSKill.infiniti.repository.PrincipalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
    public class PrincipalService {

        @Autowired
        private PrincipalRepository repository;

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

            return response;
        }
    }

