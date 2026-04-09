package com.radiantSKill.infiniti.controllers;

import com.radiantSKill.infiniti.dto.*;
import com.radiantSKill.infiniti.services.ThesisResearchService;
import com.radiantSKill.infiniti.services.ThesisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/student/thesis")
@RequiredArgsConstructor
public class ThesisController {

    private final ThesisResearchService thesisResearchService;
    private final ThesisService thesisService;

    // 1️⃣ Thesis Registration
    @PostMapping("/registration")
    public ResponseEntity<ApiResponse<?>> registerThesis(
            @RequestBody ThesisRegistrationRequest request,
            Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized: Please login first", null));
        }

        thesisService.saveThesisRegistration(auth.getName(), request);

        return ResponseEntity.ok(
                new ApiResponse<>("success", "Thesis registration saved", null)
        );
    }

    @GetMapping("/registration")
    public ResponseEntity<ApiResponse<ThesisRegistrationResponseDTO>> getThesis(
            Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized: Please login first", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "success",
                        "Thesis fetched successfully",
                        thesisService.getThesisRegistration(auth.getName())
                )
        );
    }

    //Thesis-Research
    @PostMapping("/research")
    public ResponseEntity<ApiResponse<?>> saveResearch(
            @RequestBody ResearchRequest request,
            Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        thesisResearchService.saveOrUpdateResearch(auth.getName(), request);

        return ResponseEntity.ok(
                new ApiResponse<>("success", "Research saved", null)
        );
    }

    @GetMapping("/research")
    public ResponseEntity<ApiResponse<?>> getResearch(Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>("success", "Research fetched",
                        thesisResearchService.getResearch(auth.getName()))
        );
    }

    // 2️⃣ Proof of Concept
    @PostMapping("/proof-of-concept")
    public ResponseEntity<ApiResponse<?>> savePOC(
            @RequestBody ProofOfConceptRequest request,
            Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        thesisService.saveProofOfConcept(auth.getName(), request);

        return ResponseEntity.ok(
                new ApiResponse<>("success", "Proof of Concept saved", null)
        );
    }

    // 3️⃣ Digital Prototype (optional)
    @PostMapping(value = "/digital-prototype", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> uploadDP(
            @RequestParam String description,
            @RequestPart("file") MultipartFile file,
            Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        thesisService.uploadDigitalPrototype(auth.getName(), description, file);

        return ResponseEntity.ok(
                new ApiResponse<>("success", "Digital prototype uploaded", null)
        );
    }

    // 4️⃣ Financial Model (mandatory)
    @PostMapping(value = "/financial-model", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> uploadFM(
            @RequestParam String summary,
            @RequestPart("file") MultipartFile file,
            Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        thesisService.uploadFinancialModel(auth.getName(), summary, file);

        return ResponseEntity.ok(
                new ApiResponse<>("success", "Financial model uploaded", null)
        );
    }

    // 5️⃣ Thesis Presentation
    @PostMapping(value = "/presentation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> uploadPresentation(
            @RequestPart("file") MultipartFile file,
            Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        thesisService.uploadPresentation(auth.getName(), file);

        return ResponseEntity.ok(
                new ApiResponse<>("success", "Presentation uploaded", null)
        );
    }

    // 6️⃣ Selfie Video
    @PostMapping(value = "/selfie-video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> uploadSelfieVideo(
            @RequestPart("file") MultipartFile file,
            Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "Unauthorized", null));
        }

        thesisService.uploadSelfieVideo(auth.getName(), file);

        return ResponseEntity.ok(
                new ApiResponse<>("success", "Selfie video uploaded", null)
        );
    }
}
