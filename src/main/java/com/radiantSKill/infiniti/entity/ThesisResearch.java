package com.radiantSKill.infiniti.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "thesis_research")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThesisResearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Student mapping
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private AppUser student;

    // ✅ Topic mapping (this replaces thesisId + topic)
    @ManyToOne
    @JoinColumn(name = "thesis_id", nullable = false)
    private ThesisTopic topic;

    @Column(name = "research_text", columnDefinition = "TEXT", nullable = false)
    private String researchText;

    @Column(name = "status")
    private String status;

    @Column(name = "thoughts_text", columnDefinition = "TEXT")
    private String thoughtsText;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;
}