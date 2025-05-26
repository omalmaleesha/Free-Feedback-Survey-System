package com.edu.controller;


import com.edu.entity.Survey;
import com.edu.entity.Webhook;
import com.edu.repository.WebhookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/surveys/{surveyId}/webhooks")
public class WebhookController {

    @Autowired
    private WebhookRepository webhookRepository;

    @PostMapping
    public ResponseEntity<Webhook> createWebhook(@PathVariable UUID surveyId, @RequestBody Webhook webhook) {
        webhook.setSurvey(new Survey(surveyId));
        return ResponseEntity.ok(webhookRepository.save(webhook));
    }

    @GetMapping
    public ResponseEntity<List<Webhook>> getWebhooks(@PathVariable UUID surveyId) {
        return ResponseEntity.ok(webhookRepository.findBySurveyId(surveyId));
    }
}
