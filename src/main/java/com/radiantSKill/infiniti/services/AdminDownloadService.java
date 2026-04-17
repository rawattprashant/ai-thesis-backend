package com.radiantSKill.infiniti.services;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AdminDownloadService {
    void downloadStudentZip(Long studentId, HttpServletResponse response) throws IOException;
}
