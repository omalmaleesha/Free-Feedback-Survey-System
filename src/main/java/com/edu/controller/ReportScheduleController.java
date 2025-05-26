package com.edu.controller;


import com.edu.entity.ReportSchedule;
import com.edu.entity.Survey;
import com.edu.repository.ReportScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/surveys/{surveyId}/reports")
public class ReportScheduleController {

    @Autowired
    private ReportScheduleRepository reportScheduleRepository;

    @PostMapping
    public ResponseEntity<ReportSchedule> createReportSchedule(@PathVariable UUID surveyId, @RequestBody ReportSchedule reportSchedule) {
        reportSchedule.setSurvey(new Survey(surveyId));
        return ResponseEntity.ok(reportScheduleRepository.save(reportSchedule));
    }

    @GetMapping
    public ResponseEntity<List<ReportSchedule>> getReportSchedules(@PathVariable UUID surveyId) {
        return ResponseEntity.ok(reportScheduleRepository.findAll());
    }
}