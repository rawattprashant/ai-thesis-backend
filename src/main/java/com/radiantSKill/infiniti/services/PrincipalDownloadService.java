package com.radiantSKill.infiniti.services;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface PrincipalDownloadService {
    void downloadStudentZip(
            Long studentId,
            String principalEmail,
            HttpServletResponse response
    ) throws IOException;
}
