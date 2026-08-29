package com.radiantSKill.infiniti.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PrincipalCommentResponseDTO {

    private Long id;

    private Long studentId;

    private String studentName;

    private Long principalId;

    private String principalName;

    private String schoolName;

    private String comment;

    private LocalDateTime updatedAt;
}