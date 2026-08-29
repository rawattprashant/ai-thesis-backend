package com.radiantSKill.infiniti.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrincipalCommentRequestDTO {

    @NotNull(message = "Student Id is required")
    private Long studentId;

    @NotBlank(message = "Comment is required")
    private String comment;

}