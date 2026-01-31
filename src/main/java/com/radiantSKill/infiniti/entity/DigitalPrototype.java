package com.radiantSKill.infiniti.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

// DigitalPrototype.java
@Entity
@Table(name = "digital_prototype_store")
@Getter
@Setter
public class DigitalPrototype {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "student_id", unique = true)
    private AppUser student;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String fileUrl;
    private String status;

    @CreationTimestamp
    private LocalDateTime uploadedAt;

}
