package com.radiantSKill.infiniti.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "financial_model")
@Getter
@Setter
public class FinancialModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_id", unique = true)
    private AppUser student;

    @Column(columnDefinition = "TEXT")
    private String learningSummary;

    private String fileUrl;
    private String status;

    @CreationTimestamp
    private LocalDateTime uploadedAt;
}
