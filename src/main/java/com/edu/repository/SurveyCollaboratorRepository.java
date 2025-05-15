package com.edu.repository;

import com.edu.entity.SurveyCollaborator;
import com.edu.entity.Survey;
import com.edu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SurveyCollaboratorRepository extends JpaRepository<SurveyCollaborator, UUID> {
    List<SurveyCollaborator> findBySurvey(Survey survey);
    Optional<SurveyCollaborator> findBySurveyAndUser(Survey survey, User user);
}