package com.radiantSKill.infiniti.repository;

import com.radiantSKill.infiniti.entity.ThesisTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ThesisTopicRepository extends JpaRepository<ThesisTopic, Long> {

    Optional<ThesisTopic> findByNameIgnoreCase(String name);

    @Query("SELECT COALESCE(MAX(t.id),0) FROM ThesisTopic t")
    Long getMaxId();

    @Query("SELECT t.id, t.name FROM ThesisTopic t ORDER BY t.name ASC")
    List<Object[]> fetchAllTopics();

}