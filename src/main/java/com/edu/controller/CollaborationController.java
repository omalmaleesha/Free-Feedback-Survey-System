package com.edu.controller;

import com.edu.entity.CollaboratorRole;
import com.edu.service.CollaborationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/surveys")
public class CollaborationController {

    @Autowired
    private CollaborationService collaborationService;

    @PostMapping("/{id}/collaborators")
    @PreAuthorize("@collaborationService.hasRoleForSurvey(#id, 'OWNER')")
    public ResponseEntity<Void> addCollaborator(@PathVariable UUID id, @RequestParam UUID userId, @RequestParam String role) {
        collaborationService.addCollaborator(id, userId, CollaboratorRole.valueOf(role.toUpperCase()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/collaborators/{userId}")
    @PreAuthorize("@collaborationService.hasRoleForSurvey(#id, 'OWNER')")
    public ResponseEntity<Void> removeCollaborator(@PathVariable UUID id, @PathVariable UUID userId) {
        collaborationService.removeCollaborator(id, userId);
        return ResponseEntity.ok().build();
    }
}
