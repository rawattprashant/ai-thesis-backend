package com.radiantSKill.infiniti.services;

import com.radiantSKill.infiniti.dto.ResearchRequest;

public interface ThesisResearchService {
    void saveOrUpdateResearch(String email, ResearchRequest request);
}
