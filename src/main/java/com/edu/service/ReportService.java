package com.edu.service;

import com.feedbacksurvey.entity.Frequency;
import com.feedbacksurvey.entity.ReportSchedule;
import com.feedbacksurvey.repository.ReportScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportSchedulerService {

    @Autowired
    private ReportScheduleRepository reportScheduleRepository;

    @Autowired
    private ReportService reportService;

    @Scheduled(cron = "0 0 0 * * ?") // Daily at midnight
    public void generateDailyReports() {
        List<ReportSchedule> schedules = reportScheduleRepository.findAll();
        for (ReportSchedule schedule : schedules) {
            if (schedule.getFrequency() == Frequency.DAILY) {
                reportService.generateReport(schedule.getSurvey(), schedule.getFormat());
                // Placeholder for email delivery logic
                System.out.println("Daily report generated for survey: " + schedule.getSurvey().getTitle());
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * MON") // Weekly on Monday
    public void generateWeeklyReports() {
        List<ReportSchedule> schedules = reportScheduleRepository.findAll();
        for (ReportSchedule schedule : schedules) {
            if (schedule.getFrequency() == Frequency.WEEKLY) {
                reportService.generateReport(schedule.getSurvey(), schedule.getFormat());
                // Placeholder for email delivery logic
                System.out.println("Weekly report generated for survey: " + schedule.getSurvey().getTitle());
            }
        }
    }
}