package com.radiantSKill.infiniti.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResearchRequest {

    @NotNull
    private Long thesisId;

    @NotBlank
    private String topic;

    @NotBlank
    @Size(max = 8000) // approx 1000 words
    private String researchText;

    @Size(max = 8000)
    private String thoughtsText;
}