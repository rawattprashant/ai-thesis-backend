package com.radiantSKill.infiniti.services;

import com.radiantSKill.infiniti.entity.*;
import com.radiantSKill.infiniti.repository.*;

import com.radiantSKill.infiniti.dto.ThesisRegistrationRequest;
import com.radiantSKill.infiniti.dto.ProofOfConceptRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ThesisService {

    private final AppUserRepository userRepository;
    private final ThesisRegistrationRepository thesisRegistrationRepository;
    private final ProofOfConceptRepository proofOfConceptRepository;
    private final DigitalPrototypeRepository digitalPrototypeRepository;
    private final FinancialModelRepository financialModelRepository;
    private final ThesisPresentationRepository thesisPresentationRepository;
    private final SelfieVideoRepository selfieVideoRepository;
    private final FileStorageService fileStorageService;

    /* -------------------------------
       1️⃣ THESIS REGISTRATION
     -------------------------------- */
    public void saveThesisRegistration(String email, ThesisRegistrationRequest req) {

        AppUser student = getStudent(email);

        ThesisRegistration tr = thesisRegistrationRepository
                .findByStudent(student)
                .orElse(new ThesisRegistration());

        tr.setStudent(student);
        tr.setSchoolName(req.getSchoolName());
        tr.setGender(req.getGender());
        tr.setDateOfBirth(req.getDateOfBirth());
        tr.setGrade(req.getGrade());
        tr.setSection(req.getSection());
        tr.setStudentEmail(student.getEmail());
        tr.setStudentMobile(req.getStudentMobile());
        tr.setParentEmail(req.getParentEmail());
        tr.setParentMobile(req.getParentMobile());
        tr.setThesisTopic(req.getThesisTopic());
        tr.setThesisIntent(req.getThesisIntent());
        tr.setHasDigitalPrototype(req.getHasDigitalPrototype());
        tr.setHasInvestorInterest(req.getHasInvestorInterest());
        tr.setStatus("SUBMITTED");

        thesisRegistrationRepository.save(tr);
    }

    /* -------------------------------
       2️⃣ PROOF OF CONCEPT
     -------------------------------- */
    public void saveProofOfConcept(String email, ProofOfConceptRequest req) {

        AppUser student = getStudent(email);
        ensureRegistrationCompleted(student);

        ProofOfConcept poc = proofOfConceptRepository
                .findByStudent(student)
                .orElse(new ProofOfConcept());

        poc.setStudent(student);
        poc.setContent(req.getContent());
        poc.setStatus("SUBMITTED");

        proofOfConceptRepository.save(poc);
    }

    /* -------------------------------
       3️⃣ DIGITAL PROTOTYPE (OPTIONAL)
     -------------------------------- */
    public void uploadDigitalPrototype(String email, String description, MultipartFile file) {

        AppUser student = getStudent(email);
        ThesisRegistration tr = ensureRegistrationCompleted(student);

        if (!Boolean.TRUE.equals(tr.getHasDigitalPrototype())) {
            throw new IllegalStateException("Digital Prototype not opted");
        }

        ensurePOCCompleted(student);

        String fileUrl = fileStorageService.store(file, "digital-prototype");

        DigitalPrototype dp = digitalPrototypeRepository
                .findByStudent(student)
                .orElse(new DigitalPrototype());

        dp.setStudent(student);
        dp.setDescription(description);
        dp.setFileUrl(fileUrl);
        dp.setStatus("SUBMITTED");

        digitalPrototypeRepository.save(dp);
    }

    /* -------------------------------
       4️⃣ FINANCIAL MODEL (MANDATORY)
     -------------------------------- */
    public void uploadFinancialModel(String email, String summary, MultipartFile file) {

        AppUser student = getStudent(email);
        ensurePOCCompleted(student);

        String fileUrl = fileStorageService.store(file, "financial-model");

        FinancialModel fm = financialModelRepository
                .findByStudent(student)
                .orElse(new FinancialModel());

        fm.setStudent(student);
        fm.setLearningSummary(summary);
        fm.setFileUrl(fileUrl);
        fm.setStatus("SUBMITTED");

        financialModelRepository.save(fm);
    }

    /* -------------------------------
       5️⃣ THESIS PRESENTATION
     -------------------------------- */
    public void uploadPresentation(String email, MultipartFile file) {

        AppUser student = getStudent(email);
        ensureFinancialModelCompleted(student);

        String fileUrl = fileStorageService.store(file, "presentation");

        ThesisPresentation tp = thesisPresentationRepository
                .findByStudent(student)
                .orElse(new ThesisPresentation());

        tp.setStudent(student);
        tp.setFileUrl(fileUrl);
        tp.setStatus("SUBMITTED");

        thesisPresentationRepository.save(tp);
    }

    /* -------------------------------
       6️⃣ SELFIE VIDEO
     -------------------------------- */
    public void uploadSelfieVideo(String email, MultipartFile file) {

        AppUser student = getStudent(email);
        ensurePresentationCompleted(student);

        String fileUrl = fileStorageService.store(file, "selfie-video");

        SelfieVideo sv = selfieVideoRepository
                .findByStudent(student)
                .orElse(new SelfieVideo());

        sv.setStudent(student);
        sv.setFileUrl(fileUrl);
        sv.setStatus("SUBMITTED");

        selfieVideoRepository.save(sv);
    }

    /* -------------------------------
       VALIDATION HELPERS
     -------------------------------- */
    private AppUser getStudent(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private ThesisRegistration ensureRegistrationCompleted(AppUser student) {
        return thesisRegistrationRepository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Thesis Registration not completed"));
    }

    private void ensurePOCCompleted(AppUser student) {
        proofOfConceptRepository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Proof of Concept required"));
    }

    private void ensureFinancialModelCompleted(AppUser student) {
        financialModelRepository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Financial Model required"));
    }

    private void ensurePresentationCompleted(AppUser student) {
        thesisPresentationRepository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Presentation required"));
    }
}
