package com.radiantSKill.infiniti.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "thesis_presentation")
@Getter
@Setter
public class ThesisPresentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_id", unique = true)
    private AppUser student;

    private String fileUrl;
    private String status;
    private String description;

    @CreationTimestamp
    private LocalDateTime uploadedAt;
}
