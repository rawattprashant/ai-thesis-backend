package com.radiantSKill.infiniti.services;

import com.radiantSKill.infiniti.dto.ResearchRequest;
import com.radiantSKill.infiniti.dto.ResearchResponse;

public interface ThesisResearchService {
    void saveOrUpdateResearch(String email, ResearchRequest request);

    ResearchResponse getResearch(String email);
}
