package com.radiantSKill.infiniti.dao;

import com.radiantSKill.infiniti.dto.DashboardFilterRequest;
import com.radiantSKill.infiniti.dto.StudentDashboardDTO;
import com.radiantSKill.infiniti.entity.StudentSubmissionStore;

import java.util.List;

public interface AdminDashboardDAO {

    List<StudentDashboardDTO> getStudents(
            DashboardFilterRequest filter,
            int page,
            int size
    );

    List<StudentSubmissionStore> getAllSubmissions(
            DashboardFilterRequest filter,
            int page,
            int size
    );

}