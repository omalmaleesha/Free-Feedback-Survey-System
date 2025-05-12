package com.edu.repository;

import com.edu.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

import java.util.UUID;

public interface SurveyRepository extends JpaRepository<Survey, UUID> {
}
