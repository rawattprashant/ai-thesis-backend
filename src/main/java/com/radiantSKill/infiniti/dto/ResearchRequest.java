package com.radiantSKill.infiniti.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResearchRequest {

    private String topic; // <-- IMPORTANT (match UI)

    @NotBlank
    @Size(max = 8000)
    private String researchText;

    @Size(max = 8000)
    private String thoughtsText;
}