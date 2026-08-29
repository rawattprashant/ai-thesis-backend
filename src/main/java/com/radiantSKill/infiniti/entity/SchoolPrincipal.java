package com.radiantSKill.infiniti.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "school_principals")
public class SchoolPrincipal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "principal_name", nullable = false)
    private String principalName;

    @Column(name = "school_name", nullable = false)
    private String schoolName;

    @Column(name = "school_address", nullable = false)
    private String schoolAddress;

    @Column(name = "principal_email", nullable = false, unique = true)
    private String principalEmail;

    @Column(name = "reg_status", nullable = false)
    private String regStatus = "PENDING";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "app_user_id",
            foreignKey = @ForeignKey(name = "fk_school_principal_app_user")
    )
    private AppUser appUser;
}