package com.radiantSKill.infiniti.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ThesisFinancialModelDTO {
    private String learningSummary;
    private String fileUrl;
    private String status;
    private LocalDateTime uploadedAt;
}
