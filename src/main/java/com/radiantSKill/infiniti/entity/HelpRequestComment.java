package com.radiantSKill.infiniti.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

// RequestComment.java
@Entity
@Table(name = "request_comment")
@Getter
@Setter
public class HelpRequestComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private HelpRequest HelpRequestId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    private String comment;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
