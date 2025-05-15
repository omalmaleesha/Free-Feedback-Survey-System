package com.edu.repository;


import com.edu.entity.SurveyCollaborators;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SurveyCollaboratorRepository1 extends JpaRepository<SurveyCollaborators, UUID> {
    List<SurveyCollaborators> findBySurveyId(UUID surveyId);
}

