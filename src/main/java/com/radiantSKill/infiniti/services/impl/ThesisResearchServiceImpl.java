package com.radiantSKill.infiniti.services.impl;

import com.radiantSKill.infiniti.dto.ResearchRequest;
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

        repository.save(research);

        StudentSubmissionStore store = getStore(student);
        store.setOverallStatus("RESEARCH_COMPLETED");

        studentSubmissionStoreRepository.save(store);
    }

    private ThesisTopic resolveTopic(ResearchRequest request) {

        // Normal selection (A, B, C, D)
        if (request.getThesisId() != null) {
            return topicRepository.findById(request.getThesisId())
                    .orElseThrow(() -> new RuntimeException("Invalid topic ID"));
        }

        // OTHER case
        if ("OTHER".equalsIgnoreCase(request.getTopic())) {

            return topicRepository.findByNameIgnoreCase(request.getCustomTopic())
                    .orElseGet(() -> {
                        Long nextId = topicRepository.getMaxId() + 1;

                        ThesisTopic newTopic = new ThesisTopic();
                        newTopic.setId(nextId);
                        newTopic.setName(sanitize(request.getCustomTopic()));

                        return topicRepository.save(newTopic);
                    });
        }

        throw new RuntimeException("Invalid topic selection");
    }

    private String sanitize(String input) {
        if (input == null) return null;
        return input.replaceAll("[<>]", "");
    }

    private StudentSubmissionStore getStore(AppUser student) {
        return studentSubmissionStoreRepository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("Submission store not found"));
    }
}