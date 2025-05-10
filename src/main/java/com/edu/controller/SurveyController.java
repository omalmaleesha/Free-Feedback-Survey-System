package com.edu.controller;

import com.edu.dto.SurveyDto;
import com.edu.entity.Survey;
import com.edu.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Survey> createSurvey(@RequestBody SurveyDto surveyDto) {
        return ResponseEntity.ok(surveyService.createSurvey(surveyDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Survey> updateSurvey(@PathVariable UUID id, @RequestBody SurveyDto surveyDto) {
        return ResponseEntity.ok(surveyService.updateSurvey(id, surveyDto));
    }
}
