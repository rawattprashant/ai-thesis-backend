package com.radiantSKill.infiniti.dao;

import com.radiantSKill.infiniti.dto.AdminStudentDTO;
import com.radiantSKill.infiniti.dto.DashboardFilterRequest;
import com.radiantSKill.infiniti.dto.StudentDashboardDTO;
import com.radiantSKill.infiniti.entity.StudentSubmissionStore;

import java.util.List;

public interface AdminDashboardDAO {

    List<AdminStudentDTO> getStudents(
            DashboardFilterRequest filter,
            int page,
            int size
    );

    List<AdminStudentDTO> getAllStudents(int page, int size);

}