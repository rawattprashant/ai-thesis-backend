package com.radiantSKill.infiniti.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

// StudentSubmission.java
@Entity
@Table(name = "student_submission_store")
@Getter
@Setter
public class StudentSubmissionStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private AppUser student;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private DocumentStore documentStoreId;

    @ManyToOne
    @JoinColumn(name = "summary_id")
    private ProofOfConcept proofOfConceptId;

    @ManyToOne
    @JoinColumn(name = "presentation_id")
    private ThesisPresentation thesisPresentationId;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private SelfieVideo selfieVideoId;

    @ManyToOne
    @JoinColumn(name = "digital_prototype_id")
    private DigitalPrototype digitalPrototypeId;

    @ManyToOne
    @JoinColumn(name = "financial_model_id")
    private FinancialModel financialModelId;

    private String submissionStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
