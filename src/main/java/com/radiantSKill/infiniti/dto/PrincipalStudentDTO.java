package com.radiantSKill.infiniti.dto;

import lombok.Data;

@Data
public class PrincipalStudentDTO {
    private Long studentId;
    private String name;
    private String email;
    private String status;
    private String currentStage;

    public PrincipalStudentDTO(Long studentId, String name, String email, String status, String currentStage) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.status = status;
        this.currentStage = currentStage;
    }
}
