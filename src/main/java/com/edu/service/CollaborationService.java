package com.edu.service;

import com.edu.entity.CollaboratorRole;
import com.edu.entity.Survey;
import com.edu.entity.SurveyCollaborator;
import com.edu.entity.User;
import com.edu.repository.SurveyCollaboratorRepository;
import com.edu.repository.SurveyRepository;
import com.edu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CollaborationService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private SurveyCollaboratorRepository collaboratorRepository;

    @Autowired
    private UserRepository userRepository;

    public void addOwnerOnSurveyCreation(Survey survey) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        SurveyCollaborator owner = new SurveyCollaborator();
        owner.setSurvey(survey);
        owner.setUser(user);
        owner.setRole(CollaboratorRole.OWNER);
        collaboratorRepository.save(owner);
    }

    public void addCollaborator(UUID surveyId, UUID userId, CollaboratorRole role) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new RuntimeException("Survey not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow();
        SurveyCollaborator currentCollaborator = collaboratorRepository.findBySurveyAndUser(survey, currentUser)
                .orElseThrow(() -> new RuntimeException("Not a collaborator"));
        if (!currentCollaborator.getRole().equals(CollaboratorRole.OWNER)) {
            throw new RuntimeException("Only owners can add collaborators");
        }

        SurveyCollaborator collaborator = new SurveyCollaborator();
        collaborator.setSurvey(survey);
        collaborator.setUser(user);
        collaborator.setRole(role);
        collaboratorRepository.save(collaborator);
    }

    public void removeCollaborator(UUID surveyId, UUID userId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new RuntimeException("Survey not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername).orElseThrow();
        SurveyCollaborator currentCollaborator = collaboratorRepository.findBySurveyAndUser(survey, currentUser)
                .orElseThrow(() -> new RuntimeException("Not a collaborator"));
        if (!currentCollaborator.getRole().equals(CollaboratorRole.OWNER)) {
            throw new RuntimeException("Only owners can remove collaborators");
        }

        SurveyCollaborator collaborator = collaboratorRepository.findBySurveyAndUser(survey, user)
                .orElseThrow(() -> new RuntimeException("Collaborator not found"));
        collaboratorRepository.delete(collaborator);
    }

    public boolean hasRoleForSurvey(UUID surveyId, String requiredRole) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Survey survey = surveyRepository.findById(surveyId).orElseThrow();
        SurveyCollaborator collaborator = collaboratorRepository.findBySurveyAndUser(survey, user).orElse(null);
        if (collaborator == null) {
            return false;
        }
        String userRole = collaborator.getRole().name();
        if (requiredRole.equals("VIEWER")) {
            return true;
        } else if (requiredRole.equals("EDITOR")) {
            return userRole.equals("EDITOR") || userRole.equals("OWNER");
        } else if (requiredRole.equals("OWNER")) {
            return userRole.equals("OWNER");
        }
        return false;
    }
}
