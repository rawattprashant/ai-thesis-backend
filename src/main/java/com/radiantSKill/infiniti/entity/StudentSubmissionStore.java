package com.radiantSKill.infiniti.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_submission_store")
@Getter
@Setter
public class StudentSubmissionStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ One record per student
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private AppUser student;

    // ✅ Basic details (denormalized for fast admin queries)
    private String schoolName;
    private String grade;
    private String section;
    private String thesisTopic;

    // ✅ Stage-wise status tracking
    private String registrationStatus;
    private String pocStatus;
    private String digitalPrototypeStatus;
    private String financialModelStatus;
    private String presentationStatus;
    private String selfieVideoStatus;

    // ✅ Overall tracking
    private String overallStatus;
    private String submissionStatus;

    // ✅ File mappings
    @ManyToOne
    @JoinColumn(name = "document_id")
    private DocumentStore documentStore;

    @ManyToOne
    @JoinColumn(name = "summary_id")
    private ProofOfConcept proofOfConcept;

    @ManyToOne
    @JoinColumn(name = "presentation_id")
    private ThesisPresentation thesisPresentation;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private SelfieVideo selfieVideo;

    @ManyToOne
    @JoinColumn(name = "digital_prototype_id")
    private DigitalPrototype digitalPrototype;

    @ManyToOne
    @JoinColumn(name = "financial_model_id")
    private FinancialModel financialModel;

    // ✅ Audit fields
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}