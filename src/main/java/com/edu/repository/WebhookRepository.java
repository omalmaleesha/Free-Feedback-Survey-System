package com.edu.repository;

import com.feedbacksurvey.entity.Webhook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WebhookRepository extends JpaRepository<Webhook, UUID> {
    List<Webhook> findBySurveyId(UUID surveyId);
}
