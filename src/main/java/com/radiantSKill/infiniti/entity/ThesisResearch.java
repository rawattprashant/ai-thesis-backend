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

    @Column(name = "thesis_id", nullable = false, unique = true)
    private Long thesisId;

    @Column(nullable = false)
    private String topic;

    @Column(name = "research_text", columnDefinition = "TEXT", nullable = false)
    private String researchText;

    @Column(name = "thoughts_text", columnDefinition = "TEXT")
    private String thoughtsText;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;
}