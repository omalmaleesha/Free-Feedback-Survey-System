package com.edu.service;
import com.edu.dto.SurveyDto;
import com.edu.entity.Survey;
import com.edu.entity.SurveyCollaborators;
import com.edu.entity.User;
import com.edu.repository.SurveyCollaboratorRepository1;
import com.edu.repository.SurveyRepository;
import com.edu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private SurveyCollaboratorRepository1 collaboratorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollaborationService collaborationService;

    public boolean hasPermission(UUID surveyId, String requiredRole) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).get();
        List<SurveyCollaborators> collaborators = collaboratorRepository.findBySurveyId(surveyId);
        return collaborators.stream().anyMatch(c -> c.getUser().getId().equals(user.getId()) &&
                (c.getRole().equals("OWNER") || c.getRole().equals(requiredRole)));
    }



    public Survey createSurvey(SurveyDto surveyDto) {
        Survey survey = new Survey();
        survey.setTitle(surveyDto.getTitle());
        survey.setDescription(surveyDto.getDescription());
        survey.setPublicUuid(UUID.randomUUID());
        survey = surveyRepository.save(survey);
        collaborationService.addOwnerOnSurveyCreation(survey);
        return survey;
    }

    public Survey updateSurvey(UUID id, SurveyDto surveyDto) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new RuntimeException("Survey not found"));
        survey.setTitle(surveyDto.getTitle());
        survey.setDescription(surveyDto.getDescription());
        return surveyRepository.save(survey);
    }
}
