package com.radiantSKill.infiniti.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "proof_of_concept")
@Getter
@Setter
public class ProofOfConcept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_id", unique = true)
    private AppUser student;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String status;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
