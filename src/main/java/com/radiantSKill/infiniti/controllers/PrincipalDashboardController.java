package com.radiantSKill.infiniti.controllers;

import com.radiantSKill.infiniti.dto.*;
import com.radiantSKill.infiniti.services.PrincipalDownloadService;
import com.radiantSKill.infiniti.services.PrincipalService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/principal/dashboard")
@RequiredArgsConstructor
public class PrincipalDashboardController {
    @Autowired
    private PrincipalService principalService;
    private PrincipalDownloadService principalDownloadService;

    @PostMapping("/school")
    public ResponseEntity<?> getFilteredStudents(
            @RequestBody PrincipalRequestDTO request,
            Authentication auth
    ) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success",
                        "Schools  fetched",
                        principalService.registerSchool(request)
                )
        );
    }

    @GetMapping("/school/{id}")
    public PrincipalResponse getSchool(
            @PathVariable Long id) {
        return principalService.getSchool(id);
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<PrincipalStatsDTO>> getStats(Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }
        String principalEmail = auth.getName();

        // Fetch school name using email
        String schoolName =
                principalService.getSchoolNameByEmail(
                        principalEmail
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success",
                        "Stats fetched",
                        principalService.getStats(schoolName)
                )
        );
    }
    @GetMapping("/students")
    public ResponseEntity<ApiResponse<List<PrincipalStudentDTO>>> getAllStudents(
            @RequestParam int page,
            @RequestParam int size,
            Authentication auth
    ) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }
        String principalEmail = auth.getName();

        // Fetch school name using email
        String schoolName =
                principalService.getSchoolNameByEmail(
                        principalEmail
                );


        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success",
                        "Students fetched",
                        principalService.getAllStudents(schoolName,page, size)
                )
        );
    }
    @PostMapping("/students/filter")
    public ResponseEntity<?> getFilteredStudents(
            @RequestBody PrincipalDashboardFilterRequest filter,
            @RequestParam int page,
            @RequestParam int size,
            Authentication auth
    ) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }
        String principalEmail = auth.getName();

        // Fetch school name using email
        String schoolName =
                principalService.getSchoolNameByEmail(
                        principalEmail
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success",
                        "Filtered students fetched",
                        principalService.getStudentsLight(filter, schoolName, page, size)
                )
        );
    }
    @GetMapping("/students/{studentId}/download")
    public void downloadStudentZip(
            @PathVariable Long studentId,
            String principalEmail,
            HttpServletResponse response
    ) throws IOException {
        principalDownloadService.downloadStudentZip(studentId, principalEmail,response);
    }

}
