package com.edu.service;

import com.feedbacksurvey.entity.ReportSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportScheduleRepository extends JpaRepository<ReportSchedule, UUID> {
}
