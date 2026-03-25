package com.radiantSKill.infiniti.dao;

import com.radiantSKill.infiniti.dto.DashboardFilterRequest;
import com.radiantSKill.infiniti.dto.StudentDashboardDTO;
import java.util.List;

public interface AdminDashboardDAO {

    List<StudentDashboardDTO> getStudents(
            DashboardFilterRequest filter,
            int page,
            int size
    );

}