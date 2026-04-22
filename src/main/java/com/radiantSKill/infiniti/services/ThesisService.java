package com.radiantSKill.infiniti.services;

import com.radiantSKill.infiniti.dto.*;
import com.radiantSKill.infiniti.entity.*;
import com.radiantSKill.infiniti.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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
    private final JavaMailSender javaMailSender;
    private final ThesisTopicRepository thesisTopicRepository;
    private final StudentSubmissionStoreRepository studentSubmissionStoreRepository;
    private final ThesisResearchRepository repository;
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

        String topicName = req.getThesisTopic();

        if (req.getThesisId() != null && req.getThesisId() != 0) {

            // Predefined topic → fetch by ID
            ThesisTopic topic = thesisTopicRepository.findById(req.getThesisId())
                    .orElseThrow(() -> new RuntimeException("Invalid topic ID"));

            topicName = topic.getName();

        } else {
            // OTHER → create if not exists
            String finalTopicName = topicName;
            thesisTopicRepository.findByNameIgnoreCase(topicName.trim())
                    .orElseGet(() -> {
                        ThesisTopic newTopic = new ThesisTopic();
                        newTopic.setName(finalTopicName.trim());
                        return thesisTopicRepository.save(newTopic);
                    });
        }

        // Always store string in thesis_registration
        tr.setThesisTopic(topicName);

        tr.setThesisTopic(req.getThesisTopic());
        tr.setThesisIntent(req.getThesisIntent());
        tr.setHasDigitalPrototype(req.getHasDigitalPrototype());
        tr.setHasInvestorInterest(req.getHasInvestorInterest());
        tr.setStatus("SUBMITTED");

        thesisRegistrationRepository.save(tr);

        StudentSubmissionStore store = getStore(student);

        store.setSchoolName(req.getSchoolName());
        store.setGrade(req.getGrade());
        store.setSection(req.getSection());
        store.setThesisTopic(req.getThesisTopic());

        store.setRegistrationStatus("COMPLETED");
        store.setOverallStatus("IN_PROGRESS");

        studentSubmissionStoreRepository.save(store);
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
        String oldStatus = poc.getStatus();
        poc.setStudent(student);
        poc.setContent(req.getContent());
        poc.setStatus("SUBMITTED");

        proofOfConceptRepository.save(poc);

        if (!"SUBMITTED".equals(oldStatus)) {
            sendMilestoneEmail(student, "Proof Of Concept");
        }

        StudentSubmissionStore store = getStore(student);

        store.setPocStatus("COMPLETED");
        store.setProofOfConcept(poc);
        store.setOverallStatus("POC_COMPLETED");

        studentSubmissionStoreRepository.save(store);
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
        String oldStatus = dp.getStatus();
        dp.setStudent(student);
        dp.setDescription(description);
        dp.setFileUrl(fileUrl);
        dp.setStatus("SUBMITTED");

        digitalPrototypeRepository.save(dp);
        if (!"SUBMITTED".equals(oldStatus)) {
            sendMilestoneEmail(student, "Digital Prototype");
        }

        StudentSubmissionStore store = getStore(student);

        store.setDigitalPrototypeStatus("COMPLETED");
        store.setDigitalPrototype(dp);

        studentSubmissionStoreRepository.save(store);
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
        String oldStatus = fm.getStatus();
        fm.setStudent(student);
        fm.setLearningSummary(summary);
        fm.setFileUrl(fileUrl);
        fm.setStatus("SUBMITTED");

        financialModelRepository.save(fm);
        if (!"SUBMITTED".equals(oldStatus)) {
            sendMilestoneEmail(student, "Financial Model");
        }

        StudentSubmissionStore store = getStore(student);

        store.setFinancialModelStatus("COMPLETED");
        store.setFinancialModel(fm);
        store.setOverallStatus("FINANCIAL_MODEL_COMPLETED");

        studentSubmissionStoreRepository.save(store);
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
        String oldStatus = tp.getStatus();
        tp.setStudent(student);
        tp.setFileUrl(fileUrl);
        tp.setStatus("SUBMITTED");

        thesisPresentationRepository.save(tp);
        if (!"SUBMITTED".equals(oldStatus)) {
            sendMilestoneEmail(student, "Presentation");
        }

        StudentSubmissionStore store = getStore(student);

        store.setPresentationStatus("COMPLETED");
        store.setThesisPresentation(tp);

        studentSubmissionStoreRepository.save(store);
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
        String oldStatus = sv.getStatus();
        sv.setStudent(student);
        sv.setFileUrl(fileUrl);
        sv.setStatus("SUBMITTED");

        selfieVideoRepository.save(sv);
        if (!"SUBMITTED".equals(oldStatus)) {
            sendMilestoneEmail(student, "Selfie Video");
        }

        StudentSubmissionStore store = getStore(student);

        store.setSelfieVideoStatus("COMPLETED");
        store.setSelfieVideo(sv);

        store.setOverallStatus("COMPLETED");
        store.setSubmissionStatus("SUBMITTED");

        studentSubmissionStoreRepository.save(store);
    }
    // get api selfie video

    public ThesisSelfVideoDTO getSelfieVideo(String email){
        AppUser student = getStudent(email);

        SelfieVideo tr = selfieVideoRepository
                .findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Selfie video not found"));
        ThesisSelfVideoDTO dto = new ThesisSelfVideoDTO();

        dto.setStatus(tr.getStatus());
        dto.setFile_url(tr.getFileUrl());
        dto.setUploaded_at(tr.getUploadedAt());

        return  dto;

    }

    // get api proof of concept
    public ThesisProofOFConceptDTO getProofOfConcept(String email){
        AppUser student = getStudent(email);

        ProofOfConcept tr = proofOfConceptRepository
                .findByStudent(student)
                .orElseThrow(()  -> new RuntimeException("Proof of concept not found"));
        ThesisProofOFConceptDTO dto = new ThesisProofOFConceptDTO();

        dto.setStatus(tr.getStatus());
        dto.setContent(tr.getContent());
        dto.setUpdated_at(tr.getUpdatedAt());

        return  dto;


    }

    // get api thesis representation

    public ThesisResentationDTO getRepresentation(String email){
        AppUser student = getStudent(email);

        ThesisPresentation tr = thesisPresentationRepository
                .findByStudent(student)
                .orElseThrow(()->new RuntimeException("Presentation  not found") );

        ThesisResentationDTO dto = new ThesisResentationDTO();

        dto.setStatus(tr.getStatus());
        dto.setFile_url(tr.getFileUrl());
        dto.setUploaded_at(tr.getUploadedAt());


        return dto;
    }
// get api for financial model
    public ThesisFinancialModelDTO getFinancialModel(String email){
        AppUser student = getStudent(email);

        FinancialModel tr = financialModelRepository
                .findByStudent(student)
                .orElseThrow(()->new RuntimeException("Financial model  not found"));

        ThesisFinancialModelDTO dto = new ThesisFinancialModelDTO();

        dto.setStatus(tr.getStatus());
        dto.setFileUrl(tr.getFileUrl());
        dto.setLearningSummary(tr.getLearningSummary());
        dto.setUploadedAt(tr.getUploadedAt());


        return dto;
    }

    //get api for digital prototype

    public ThesisDigitalPrototypeDTO getDigitalPrototype(String email){
        AppUser student = getStudent(email);

        DigitalPrototype tr = digitalPrototypeRepository
                .findByStudent(student)
                .orElseThrow(()->new RuntimeException("Digital prototype  not found"));

        ThesisDigitalPrototypeDTO dto = new ThesisDigitalPrototypeDTO();

        dto.setDescription(tr.getDescription());
        dto.setStatus(tr.getStatus());
        dto.setFileUrl(tr.getFileUrl());
        dto.setUploadedAt(tr.getUploadedAt());

        return  dto;
    }



    public ThesisRegistrationResponseDTO getThesisRegistration(String email) {

        AppUser student = getStudent(email);

        ThesisRegistration tr = thesisRegistrationRepository
                .findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Thesis not found"));

        ThesisRegistrationResponseDTO dto = new ThesisRegistrationResponseDTO();

        dto.setSchoolName(tr.getSchoolName());
        dto.setGender(tr.getGender());
        dto.setDateOfBirth(tr.getDateOfBirth());
        dto.setGrade(tr.getGrade());
        dto.setSection(tr.getSection());

        dto.setStudentEmail(tr.getStudentEmail());
        dto.setStudentMobile(tr.getStudentMobile());

        dto.setParentEmail(tr.getParentEmail());
        dto.setParentMobile(tr.getParentMobile());

        dto.setThesisTopic(tr.getThesisTopic());
        dto.setThesisIntent(tr.getThesisIntent());

        dto.setHasDigitalPrototype(tr.getHasDigitalPrototype());
        dto.setHasInvestorInterest(tr.getHasInvestorInterest());

        dto.setStatus(tr.getStatus());

        return dto;
    }

    public List<ThesisTopicDTO> getAllTopics() {
        return thesisTopicRepository.fetchAllTopics()
                .stream()
                .map(obj -> new ThesisTopicDTO(
                        (Long) obj[0],
                        (String) obj[1]
                ))
                .toList();
    }

    public void sendMail(String to, String subject, String message){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("Kalpesh@radiantskill.com");
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(message);

        javaMailSender.send(mail);

    }
    private void sendMilestoneEmail(AppUser student, String milestoneName) {

        ThesisRegistration reg = ensureRegistrationCompleted(student);

        String studentSubject = "Milestone Completed - " + milestoneName;

        String studentBody =
                "Dear Student,\n\n" +
                        "Congratulations! 🎉\n\n" +
                        "You have successfully completed the \"" + milestoneName + "\" milestone of your thesis project.\n" +
                        "This is an important step in your academic journey and reflects your dedication and effort.\n\n" +
                        "Keep working with the same enthusiasm as you move forward to the next milestones.\n\n" +
                        "Best regards,\n" +
                        "Team Infiniti";

        // 3. Parent Email Content
        String parentSubject = "Milestone Update - " + milestoneName + " Completed";

        String parentBody =
                "Dear Parent,\n\n" +
                        "We are pleased to inform you that your child has successfully completed the \"" + milestoneName + "\" milestone.\n\n" +
                        "This achievement marks steady progress in their thesis journey and showcases their commitment.\n\n" +
                        "We appreciate your continued support and encouragement in helping them reach their goals.\n\n" +
                        "Best regards,\n" +
                        "Team Infiniti";

        sendMail(reg.getStudentEmail(), studentSubject, studentBody);

        if (reg.getParentEmail() != null) {
            sendMail(reg.getParentEmail(), parentSubject, parentBody);
        }
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

    private StudentSubmissionStore getStore(AppUser student) {
        return studentSubmissionStoreRepository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Submission store not found"));
    }
}
