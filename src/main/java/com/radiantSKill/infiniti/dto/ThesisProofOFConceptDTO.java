package com.radiantSKill.infiniti.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ThesisProofOFConceptDTO {
    private String content;
    private String status;
    private LocalDateTime updated_at;
}
