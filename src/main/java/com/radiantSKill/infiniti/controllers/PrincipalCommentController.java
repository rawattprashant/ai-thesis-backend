package com.radiantSKill.infiniti.controllers;

import com.radiantSKill.infiniti.dto.ApiResponse;
import com.radiantSKill.infiniti.dto.PrincipalCommentRequestDTO;
import com.radiantSKill.infiniti.dto.PrincipalCommentResponseDTO;
import com.radiantSKill.infiniti.services.PrincipalCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/principal/comments")
@RequiredArgsConstructor
public class PrincipalCommentController {

    private final PrincipalCommentService principalCommentService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> saveComment(
            @Valid @RequestBody PrincipalCommentRequestDTO request,
            Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(
                            "error",
                            "Unauthorized: Please login first",
                            null
                    ));
        }

        principalCommentService.saveOrUpdateComment(
                auth.getName(),
                request
        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success",
                        "Comment saved successfully",
                        null
                )
        );
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<PrincipalCommentResponseDTO>>
    getCommentByStudentId(
            @PathVariable Long studentId,
            Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(
                            "error",
                            "Unauthorized: Please login first",
                            null
                    ));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success",
                        "Comment fetched successfully",
                        principalCommentService.getCommentByStudentId(studentId)
                )
        );
    }
}