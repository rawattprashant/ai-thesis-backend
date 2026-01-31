package com.radiantSKill.infiniti.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "thesis_registration")
@Getter
@Setter
public class ThesisRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private AppUser student;

    private String schoolName;
    private String gender;

    private LocalDate dateOfBirth;

    private String grade;
    private String section;

    private String studentEmail;
    private String studentMobile;

    private String parentEmail;
    private String parentMobile;

    private String thesisTopic;

    @Column(columnDefinition = "TEXT")
    private String thesisIntent;

    private Boolean hasDigitalPrototype;
    private Boolean hasInvestorInterest;

    // DRAFT / SUBMITTED / APPROVED / REJECTED
    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
