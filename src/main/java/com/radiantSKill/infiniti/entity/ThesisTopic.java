package com.radiantSKill.infiniti.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "thesis_topic")
@Data
public class ThesisTopic {

//    @Id
//    private Long id; // IMPORTANT: no auto-gen since you inserted manually

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
}