package com.radiantSKill.infiniti.controllers;

import com.radiantSKill.infiniti.dto.*;
import com.radiantSKill.infiniti.services.AdminDashboardService;
import com.radiantSKill.infiniti.services.AdminDownloadService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;
    private final AdminDownloadService adminDownloadService;

    // ✅ 1. SUMMARY STATS
    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success",
                        "Summary fetched",
                        dashboardService.getSummary()
                )
        );
    }

    // ✅ 2. LIST ALL STUDENTS (NO FILTER)
    @GetMapping("/students")
    public ResponseEntity<ApiResponse<List<AdminStudentDTO>>> getAllStudents(
            @RequestParam int page,
            @RequestParam int size,
            Authentication auth
    ) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success",
                        "Students fetched",
                        dashboardService.getAllStudents(page, size)
                )
        );
    }

    // ✅ 3. LIST STUDENTS WITH FILTER
    @PostMapping("/students/filter")
    public ResponseEntity<?> getFilteredStudents(
            @RequestBody DashboardFilterRequest filter,
            @RequestParam int page,
            @RequestParam int size,
            Authentication auth
    ) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success",
                        "Filtered students fetched",
                        dashboardService.getStudentsLight(filter, page, size)
                )
        );
    }

    // ✅ 4. Dashboard Stats API
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsDTO>> getStats(Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success",
                        "Stats fetched",
                        dashboardService.getStats()
                )
        );
    }

    // ✅ 5. DOWNLOAD ZIP
    @GetMapping("/students/{studentId}/download")
    public void downloadStudentZip(
            @PathVariable Long studentId,
            HttpServletResponse response
    ) throws IOException {

        adminDownloadService.downloadStudentZip(studentId, response);
    }
}