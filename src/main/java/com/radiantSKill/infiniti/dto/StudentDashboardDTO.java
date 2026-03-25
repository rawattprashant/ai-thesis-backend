package com.radiantSKill.infiniti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentDashboardDTO {

    private Long studentId;

    private String name;

    private String school;

    private String grade;

    private Boolean prototype;

    private Boolean investment;
}