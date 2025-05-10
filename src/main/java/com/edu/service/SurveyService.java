package com.edu.service;
import com.edu.dto.SurveyDto;
import com.edu.entity.Survey;
import com.edu.entity.SurveyCollaborator;
import com.edu.entity.User;
import com.edu.repository.SurveyCollaboratorRepository;
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
    private SurveyCollaboratorRepository collaboratorRepository;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('USER')")
    public Survey createSurvey(SurveyDto surveyDto) {
        Survey survey = new Survey();
        survey.setTitle(surveyDto.getTitle());
        survey.setDescription(surveyDto.getDescription());
        survey.setPublicUuid(UUID.randomUUID());
        survey = surveyRepository.save(survey);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).get();

        SurveyCollaborator owner = new SurveyCollaborator();
        owner.setSurvey(survey);
        owner.setUser(user);
        owner.setRole("OWNER");
        collaboratorRepository.save(owner);

        return survey;
    }

    @PreAuthorize("hasRole('USER') and @surveyService.hasPermission(#id, 'EDITOR')")
    public Survey updateSurvey(UUID id, SurveyDto surveyDto) {
        Survey survey = surveyRepository.findById(id).orElseThrow();
        survey.setTitle(surveyDto.getTitle());
        survey.setDescription(surveyDto.getDescription());
        return surveyRepository.save(survey);
    }

    public boolean hasPermission(UUID surveyId, String requiredRole) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).get();
        List<SurveyCollaborator> collaborators = collaboratorRepository.findBySurveyId(surveyId);
        return collaborators.stream().anyMatch(c -> c.getUser().getId().equals(user.getId()) &&
                (c.getRole().equals("OWNER") || c.getRole().equals(requiredRole)));
    }
}
