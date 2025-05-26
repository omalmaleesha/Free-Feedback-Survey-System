package com.edu.service;

import com.feedbacksurvey.entity.ReportFormat;
import com.feedbacksurvey.entity.Survey;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ReportService {

    public void generateReport(Survey survey, ReportFormat format) {
        if (format == ReportFormat.PDF) {
            generatePdfReport(survey);
        } else if (format == ReportFormat.CSV) {
            generateCsvReport(survey);
        }
    }

    private void generatePdfReport(Survey survey) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Survey Report: " + survey.getTitle());
            contentStream.endText();
            contentStream.close();
            document.save("report_" + survey.getId() + ".pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateCsvReport(Survey survey) {
        // Placeholder for CSV generation logic
        System.out.println("CSV report generated for survey: " + survey.getTitle());
    }
}