package com.radiantSKill.infiniti.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

// Document.java
@Entity
@Table(name = "document_store")
@Getter
@Setter
public class DocumentStore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    private String fileName;
    private String fileType;
    private String fileUrl;
    private Long size;

    @CreationTimestamp
    private LocalDateTime uploadedAt;
}
