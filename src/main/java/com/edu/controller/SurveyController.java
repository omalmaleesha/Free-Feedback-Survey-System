package com.edu.controller;

import com.edu.dto.AnswerDto;
import com.edu.dto.SurveyDto;
import com.edu.entity.Response;
import com.edu.entity.Survey;
import com.edu.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @PostMapping
    public ResponseEntity<SurveyDto> createSurvey(@RequestBody SurveyDto surveyDto) {
        return ResponseEntity.ok(surveyService.createSurvey(surveyDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurvey(@PathVariable UUID id, @RequestParam UUID adminToken) throws AccessDeniedException {
        return ResponseEntity.ok(surveyService.getSurvey(id, adminToken));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Survey> updateSurvey(@PathVariable UUID id, @RequestParam UUID adminToken, @RequestBody SurveyDto surveyDto) throws AccessDeniedException {
        return ResponseEntity.ok(surveyService.updateSurvey(id, adminToken, surveyDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable UUID id, @RequestParam UUID adminToken) throws AccessDeniedException {
        surveyService.deleteSurvey(id, adminToken);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/public/{publicUuid}")
    public ResponseEntity<Survey> getPublicSurvey(@PathVariable UUID publicUuid) {
        return ResponseEntity.ok(surveyService.getPublicSurvey(publicUuid));
    }

    @PostMapping("/public/{publicUuid}/responses")
    public ResponseEntity<Response> submitResponse(@PathVariable UUID publicUuid, @RequestBody List<AnswerDto> answers) {
        return ResponseEntity.ok(surveyService.submitResponse(publicUuid, answers));
    }

    @GetMapping("/{id}/responses")
    public ResponseEntity<List<Response>> getResponses(@PathVariable UUID id, @RequestParam UUID adminToken) throws AccessDeniedException {
        return ResponseEntity.ok(surveyService.getResponses(id, adminToken));
    }

    @GetMapping("/{id}/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics(@PathVariable UUID id, @RequestParam UUID adminToken) throws AccessDeniedException {
        return ResponseEntity.ok(surveyService.getAnalytics(id, adminToken));
    }
}
