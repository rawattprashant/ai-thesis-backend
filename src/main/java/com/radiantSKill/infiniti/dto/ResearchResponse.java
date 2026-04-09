package com.radiantSKill.infiniti.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResearchResponse {
    private String topic;
    private String researchText;
    private String thoughtsText;
    private String status;
    private LocalDateTime submittedAt;
}
