package com.radiantSKill.infiniti.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "principal_student_comment_history")
public class PrincipalStudentCommentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "comment_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_comment_history_comment")
    )
    private PrincipalStudentComment comment;

    @Column(name = "old_comment", nullable = false, columnDefinition = "TEXT")
    private String oldComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "modified_by",
            foreignKey = @ForeignKey(name = "fk_comment_history_user")
    )
    private AppUser modifiedBy;

    @CreationTimestamp
    @Column(name = "modified_at", nullable = false, updatable = false)
    private LocalDateTime modifiedAt;
}