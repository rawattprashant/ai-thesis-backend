package com.radiantSKill.infiniti.services;

import com.radiantSKill.infiniti.dao.AdminDashboardDAO;
import com.radiantSKill.infiniti.dto.*;
import com.radiantSKill.infiniti.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final AdminDashboardDAO adminDashboardDAO;

    private final AppUserRepository userRepository;

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

    public List<StudentDashboardDTO> getStudents(
            DashboardFilterRequest filter,
            int page,
            int size
    ) {

        return adminDashboardDAO.getStudents(filter,page,size);

    }

}