package com.radiantSKill.infiniti.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "principal_student_comment",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_comment",
                        columnNames = "thesis_registration_id"
                )
        }
)
public class PrincipalStudentComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * One active comment per student
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "thesis_registration_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_comment_student")
    )
    private ThesisRegistration thesisRegistration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "principal_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_comment_principal")
    )
    private SchoolPrincipal principal;

    @Column(name = "comment_text", nullable = false, columnDefinition = "TEXT")
    private String commentText;

    @Column(name = "comment_status", nullable = false, length = 20)
    private String commentStatus = "ACTIVE";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "created_by",
            foreignKey = @ForeignKey(name = "fk_comment_created_by")
    )
    private AppUser createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "updated_by",
            foreignKey = @ForeignKey(name = "fk_comment_updated_by")
    )
    private AppUser updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}