package com.radiantSKill.infiniti.services.impl;

import com.radiantSKill.infiniti.dto.ResearchRequest;
import com.radiantSKill.infiniti.dto.ResearchResponse;
import com.radiantSKill.infiniti.entity.AppUser;
import com.radiantSKill.infiniti.entity.StudentSubmissionStore;
import com.radiantSKill.infiniti.entity.ThesisResearch;
import com.radiantSKill.infiniti.entity.ThesisTopic;
import com.radiantSKill.infiniti.repository.AppUserRepository;
import com.radiantSKill.infiniti.repository.StudentSubmissionStoreRepository;
import com.radiantSKill.infiniti.repository.ThesisResearchRepository;
import com.radiantSKill.infiniti.repository.ThesisTopicRepository;
import com.radiantSKill.infiniti.services.ThesisResearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ThesisResearchServiceImpl implements ThesisResearchService {

    private final ThesisResearchRepository repository;
    private final ThesisTopicRepository topicRepository;
    private final AppUserRepository userRepository;

    private final StudentSubmissionStoreRepository studentSubmissionStoreRepository;

    @Override
    public void saveOrUpdateResearch(String email, ResearchRequest request) {

        AppUser student = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ThesisTopic topic = resolveTopic(request);

        ThesisResearch research = repository.findByStudent(student)
                .orElse(new ThesisResearch());

        research.setStudent(student);
        research.setTopic(topic);
        research.setResearchText(sanitize(request.getResearchText()));
        research.setThoughtsText(sanitize(request.getThoughtsText()));
        research.setSubmittedAt(LocalDateTime.now());
        research.setStatus("SUBMITTED");

        repository.save(research);

        StudentSubmissionStore store = getStore(student);
        store.setResearchStatus("COMPLETED");
        store.setThesisResearch(research);
        store.setOverallStatus("RESEARCH_COMPLETED");

        studentSubmissionStoreRepository.save(store);
    }

    private ThesisTopic resolveTopic(ResearchRequest request) {

        // Case 1: Topic selected from dropdown
        if (request.getTopic() != null && !"OTHER".equalsIgnoreCase(request.getTopic())) {
            return topicRepository.findByNameIgnoreCase(request.getTopic())
                    .orElseThrow(() -> new RuntimeException("Invalid topic"));
        }

        // Case 2: Custom topic (OTHER)
        if ("OTHER".equalsIgnoreCase(request.getTopic())) {

            return topicRepository.findByNameIgnoreCase(request.getCustomTopic())
                    .orElseGet(() -> {
                        ThesisTopic newTopic = new ThesisTopic();
                        newTopic.setName(request.getCustomTopic());
                        return topicRepository.save(newTopic);
                    });
        }

        throw new RuntimeException("Topic is required");
    }

    private String sanitize(String input) {
        if (input == null) return null;
        return input.replaceAll("[<>]", "");
    }

    private StudentSubmissionStore getStore(AppUser student) {
        return studentSubmissionStoreRepository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Submission store not found"));
    }

    public ResearchResponse getResearch(String email) {

        AppUser student = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ThesisResearch research = repository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Research not found"));

        return ResearchResponse.builder()
                .topic(research.getTopic().getName())
                .researchText(research.getResearchText())
                .thoughtsText(research.getThoughtsText())
                .status(research.getStatus())
                .submittedAt(research.getSubmittedAt())
                .build();
    }
}