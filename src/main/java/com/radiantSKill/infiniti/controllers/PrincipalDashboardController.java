package com.radiantSKill.infiniti.controllers;

import com.radiantSKill.infiniti.dto.ApiResponse;
import com.radiantSKill.infiniti.dto.PrincipalRequestDTO;
import com.radiantSKill.infiniti.dto.PrincipalResponse;
import com.radiantSKill.infiniti.services.PrincipalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/principal/dashboard")
@RequiredArgsConstructor
public class PrincipalDashboardController {
    @Autowired
    private PrincipalService principalService;

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
}
