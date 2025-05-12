package com.edu.repository;


import com.edu.entity.SurveyCollaborator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SurveyCollaboratorRepository extends JpaRepository<SurveyCollaborator, UUID> {
    List<SurveyCollaborator> findBySurveyId(UUID surveyId);
}

