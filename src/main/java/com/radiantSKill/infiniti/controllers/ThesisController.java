package com.radiantSKill.infiniti.controllers;

import com.radiantSKill.infiniti.dto.ProofOfConceptRequest;
import com.radiantSKill.infiniti.dto.ThesisRegistrationRequest;
import com.radiantSKill.infiniti.services.ThesisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/student/thesis")
@RequiredArgsConstructor
public class ThesisController {

    private final ThesisService thesisService;

    // 1️⃣ Thesis Registration
    @PostMapping("/registration")
    public ResponseEntity<?> registerThesis(
            @RequestBody ThesisRegistrationRequest request,
            Authentication auth) {
        thesisService.saveThesisRegistration(auth.getName(), request);
        return ResponseEntity.ok("Thesis registration saved");
    }

    // 2️⃣ Proof of Concept
    @PostMapping("/proof-of-concept")
    public ResponseEntity<?> saveProofOfConcept(
            @RequestBody ProofOfConceptRequest request,
            Authentication auth) {
        thesisService.saveProofOfConcept(auth.getName(), request);
        return ResponseEntity.ok("Proof of Concept saved");
    }

    // 3️⃣ Digital Prototype (optional)
    @PostMapping("/digital-prototype")
    public ResponseEntity<?> uploadDigitalPrototype(
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file,
            Authentication auth) {
        thesisService.uploadDigitalPrototype(auth.getName(), description, file);
        return ResponseEntity.ok("Digital prototype uploaded");
    }

    // 4️⃣ Financial Model (mandatory)
    @PostMapping("/financial-model")
    public ResponseEntity<?> uploadFinancialModel(
            @RequestParam("summary") String summary,
            @RequestParam("file") MultipartFile file,
            Authentication auth) {
        thesisService.uploadFinancialModel(auth.getName(), summary, file);
        return ResponseEntity.ok("Financial model uploaded");
    }

    // 5️⃣ Thesis Presentation
    @PostMapping("/presentation")
    public ResponseEntity<?> uploadPresentation(
            @RequestParam("file") MultipartFile file,
            Authentication auth) {
        thesisService.uploadPresentation(auth.getName(), file);
        return ResponseEntity.ok("Presentation uploaded");
    }

    // 6️⃣ Selfie Video
    @PostMapping("/selfie-video")
    public ResponseEntity<?> uploadSelfieVideo(
            @RequestParam("file") MultipartFile file,
            Authentication auth) {
        thesisService.uploadSelfieVideo(auth.getName(), file);
        return ResponseEntity.ok("Selfie video uploaded");
    }
}
