package com.radiantSKill.infiniti.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResearchRequest {

    // ✅ For normal topics (A, B, C, D)
    private Long thesisId;

    // ✅ Will be "OTHER" when custom topic is selected
    private String topic;

    // ✅ Custom topic name (E, F, etc.)
    private String customTopic;

    @NotBlank
    @Size(max = 8000)
    private String researchText;

    @Size(max = 8000)
    private String thoughtsText;
}