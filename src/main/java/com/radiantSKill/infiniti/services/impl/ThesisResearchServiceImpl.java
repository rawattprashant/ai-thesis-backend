package com.radiantSKill.infiniti.services.impl;

import com.radiantSKill.infiniti.dto.ResearchRequest;
import com.radiantSKill.infiniti.entity.ThesisResearch;
import com.radiantSKill.infiniti.repository.ThesisResearchRepository;
import com.radiantSKill.infiniti.services.ThesisResearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ThesisResearchServiceImpl implements ThesisResearchService {

    private final ThesisResearchRepository repository;

    @Override
    public void saveOrUpdateResearch(String email, ResearchRequest request) {

        ThesisResearch research = repository.findByThesisId(request.getThesisId())
                .orElse(new ThesisResearch());

        research.setThesisId(request.getThesisId());
        research.setTopic(sanitize(request.getTopic()));
        research.setResearchText(sanitize(request.getResearchText()));
        research.setThoughtsText(sanitize(request.getThoughtsText()));
        research.setSubmittedAt(LocalDateTime.now());

        repository.save(research);
    }

    // Basic sanitization (plain text only)
    private String sanitize(String input) {
        if (input == null) return null;
        return input.replaceAll("[<>]", ""); // removes HTML tags
    }
}