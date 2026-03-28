package com.radiantSKill.infiniti.controllers;

import com.radiantSKill.infiniti.dto.ProofOfConceptRequest;
import com.radiantSKill.infiniti.dto.ResearchRequest;
import com.radiantSKill.infiniti.dto.ThesisRegistrationRequest;
import com.radiantSKill.infiniti.services.ThesisResearchService;
import com.radiantSKill.infiniti.services.ThesisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/student/thesis")
@RequiredArgsConstructor
public class ThesisController {

    private final ThesisResearchService thesisResearchService;
    private final ThesisService thesisService;

    // 1️⃣ Thesis Registration
    @PostMapping("/registration")
    public ResponseEntity<?> registerThesis(
            @RequestBody ThesisRegistrationRequest request,
            Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(Map.of(
                            "status", "error",
                            "message", "Unauthorized: Please login first"
                    ));
        }

        thesisService.saveThesisRegistration(auth.getName(), request);
        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "message", "Thesis registration saved"
                )
        );
    }

    //Thesis-Research
    @PostMapping("/research")
    public ResponseEntity<?> saveResearch(
            @RequestBody @Valid ResearchRequest request,
            Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(Map.of(
                            "status", "error",
                            "message", "Unauthorized: Please login first"
                    ));
        }

        thesisResearchService.saveOrUpdateResearch(auth.getName(), request);

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "message", "Research milestone saved"
                )
        );
    }

    // 2️⃣ Proof of Concept
    @PostMapping("/proof-of-concept")
    public ResponseEntity<?> saveProofOfConcept(
            @RequestBody ProofOfConceptRequest request,
            Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(Map.of(
                            "status", "error",
                            "message", "Unauthorized: Please login first"
                    ));
        }
        thesisService.saveProofOfConcept(auth.getName(), request);
        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "message", "Proof of Concept saved"
                ));
    }

    // 3️⃣ Digital Prototype (optional)
    @PostMapping("/digital-prototype")
    public ResponseEntity<?> uploadDigitalPrototype(
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file,
            Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(Map.of(
                            "status", "error",
                            "message", "Unauthorized: Please login first"
                    ));
        }
        thesisService.uploadDigitalPrototype(auth.getName(), description, file);
        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "message", "Digital prototype uploaded"
                ));
    }

    // 4️⃣ Financial Model (mandatory)
    @PostMapping("/financial-model")
    public ResponseEntity<?> uploadFinancialModel(
            @RequestParam("summary") String summary,
            @RequestParam("file") MultipartFile file,
            Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(Map.of(
                            "status", "error",
                            "message", "Unauthorized: Please login first"
                    ));
        }
        thesisService.uploadFinancialModel(auth.getName(), summary, file);
        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "message", "Financial model uploaded"
                ));
    }

    // 5️⃣ Thesis Presentation
    @PostMapping("/presentation")
    public ResponseEntity<?> uploadPresentation(
            @RequestParam("file") MultipartFile file,
            Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(Map.of(
                            "status", "error",
                            "message", "Unauthorized: Please login first"
                    ));
        }
        thesisService.uploadPresentation(auth.getName(), file);
        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "message", "Presentation uploaded"
                ));
    }

    // 6️⃣ Selfie Video
    @PostMapping("/selfie-video")
    public ResponseEntity<?> uploadSelfieVideo(
            @RequestParam("file") MultipartFile file,
            Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(Map.of(
                            "status", "error",
                            "message", "Unauthorized: Please login first"
                    ));
        }
        thesisService.uploadSelfieVideo(auth.getName(), file);
        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "message", "Selfie video uploaded"
                ));
    }
}
