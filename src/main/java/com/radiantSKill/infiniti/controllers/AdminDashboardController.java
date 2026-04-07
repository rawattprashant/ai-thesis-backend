package com.radiantSKill.infiniti.controllers;

import com.radiantSKill.infiniti.dto.*;
import com.radiantSKill.infiniti.services.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    // ✅ FILTER API (MAIN)
    @PostMapping("/submissions/filter")
    public ResponseEntity<?> filter(
            @RequestBody AdminFilterRequest request,
            Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        Page<?> result = dashboardService.filter(request);

        return ResponseEntity.ok(
                new ApiResponse<>("success", "Filtered data fetched", result)
        );
    }

    // ✅ STATS API
    @GetMapping("/stats")
    public ResponseEntity<?> stats(Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>("success", "Stats fetched", dashboardService.getStats())
        );
    }

    // ✅ SUMMARY API
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<AdminDashboardSummaryDTO>> getSummary(Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success",
                        "Dashboard summary fetched",
                        dashboardService.getSummary()
                )
        );
    }

    // ✅ FILTERED STUDENTS (with pagination)
    @PostMapping("/students/filter")
    public ResponseEntity<?> students(
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
                        "Students fetched",
                        dashboardService.getStudents(filter, page, size)
                )
        );
    }

    // ✅ ALL STUDENTS (no filter)
    @GetMapping("/students")
    public ResponseEntity<ApiResponse<List<AdminStudentDTO>>> getStudents(Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success",
                        "Students fetched",
                        dashboardService.getStudents()
                )
        );
    }
}