package com.radiantSKill.infiniti.dao;

import com.radiantSKill.infiniti.dto.PrincipalDashboardFilterRequest;
import com.radiantSKill.infiniti.dto.PrincipalRequestDTO;
import com.radiantSKill.infiniti.dto.PrincipalStudentDTO;

import java.util.List;

public interface PrincipalDashboardDAO {


    List<PrincipalStudentDTO> getStudentsBySchool(
            String schoolName,
            int page,
            int size
    );
    List<PrincipalStudentDTO> getStudents(
            String schoolName,
            PrincipalDashboardFilterRequest filter,
            int page,
            int size
    );
}


