package com.edu.service;

import com.edu.dto.AnswerDto;
import com.edu.dto.SurveyDto;
import com.edu.entity.Answer;
import com.edu.entity.Question;
import com.edu.entity.Response;
import com.edu.entity.Survey;
import com.edu.repository.AnswerRepository;
import com.edu.repository.QuestionRepository;
import com.edu.repository.ResponseRepository;
import com.edu.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SurveyService01 {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public SurveyDto createSurvey(SurveyDto surveyDto) {
        Survey survey = new Survey();
        survey.setTitle(surveyDto.getTitle());
        survey.setDescription(surveyDto.getDescription());
        survey.setPublicUuid(UUID.randomUUID());
        survey.setAdminToken(UUID.randomUUID());

        Survey finalSurvey = survey;
        List<Question> questions = surveyDto.getQuestions().stream().map(q -> {
            Question question = new Question();
            question.setSurvey(finalSurvey);
            question.setQuestionText(q.getQuestionText());
            question.setType(q.getType());
            if (q.getType() == Question.QuestionType.MCQ) {
                question.setOptions(q.getOptions());
            }
            return question;
        }).collect(Collectors.toList());

        survey.setQuestions(questions);
        survey = surveyRepository.save(survey);

        SurveyDto responseDto = new SurveyDto();
        responseDto.setId(survey.getId());
        responseDto.setTitle(survey.getTitle());
        responseDto.setDescription(survey.getDescription());
        responseDto.setPublicUuid(survey.getPublicUuid());
        responseDto.setAdminToken(survey.getAdminToken());
        responseDto.setQuestions(surveyDto.getQuestions());
        return responseDto;
    }

    public Survey getSurvey(UUID id, UUID adminToken) throws AccessDeniedException {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found"));
        if (!survey.getAdminToken().equals(adminToken)) {
            throw new AccessDeniedException("Invalid admin token");
        }
        return survey;
    }

    public Survey getPublicSurvey(UUID publicUuid) {
        return surveyRepository.findByPublicUuid(publicUuid)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found"));
    }

    public Survey updateSurvey(UUID id, UUID adminToken, SurveyDto surveyDto) throws AccessDeniedException {
        Survey survey = getSurvey(id, adminToken);
        survey.setTitle(surveyDto.getTitle());
        survey.setDescription(surveyDto.getDescription());
        return surveyRepository.save(survey);
    }

    public void deleteSurvey(UUID id, UUID adminToken) throws AccessDeniedException {
        Survey survey = getSurvey(id, adminToken);
        surveyRepository.delete(survey);
    }

    public Response submitResponse(UUID publicUuid, List<AnswerDto> answersDto) {
        Survey survey = getPublicSurvey(publicUuid);
        Response response = new Response();
        response.setSurvey(survey);
        response.setSubmittedAt(LocalDateTime.now());

        List<Answer> answers = answersDto.stream().map(a -> {
            Question question = questionRepository.findById(a.getQuestionId())
                    .orElseThrow(() -> new IllegalArgumentException("Question not found"));
            if (!question.getSurvey().getId().equals(survey.getId())) {
                throw new IllegalArgumentException("Question does not belong to this survey");
            }
            Answer answer = new Answer();
            answer.setResponse(response);
            answer.setQuestion(question);
            answer.setValue(a.getValue());
            return answer;
        }).collect(Collectors.toList());

        response.setAnswers(answers);
        return responseRepository.save(response);
    }

    public List<Response> getResponses(UUID id, UUID adminToken) throws AccessDeniedException {
        Survey survey = getSurvey(id, adminToken);
        return responseRepository.findAll().stream()
                .filter(r -> r.getSurvey().getId().equals(survey.getId()))
                .collect(Collectors.toList());
    }

    public Map<String, Object> getAnalytics(UUID surveyId, UUID adminToken) throws AccessDeniedException {
        Survey survey = getSurvey(surveyId, adminToken);
        List<Question> questions = survey.getQuestions();
        Map<String, Object> analytics = new HashMap<>();

        for (Question question : questions) {
            List<Answer> answers = answerRepository.findByQuestion(question);
            Map<String, Long> responseCounts = answers.stream()
                    .collect(Collectors.groupingBy(Answer::getValue, Collectors.counting()));

            String chartUrl = generateChartUrl(question, responseCounts);

            analytics.put(question.getId().toString(), Map.of(
                    "questionText", question.getQuestionText(),
                    "type", question.getType(),
                    "responses", responseCounts,
                    "chartUrl", chartUrl
            ));
        }

        return analytics;
    }

    private String generateChartUrl(Question question, Map<String, Long> responseCounts) {
        if (question.getType() == Question.QuestionType.MCQ) {
            String labels = question.getOptions().stream()
                    .map(s -> "'" + s + "'")
                    .collect(Collectors.joining(","));
            String data = question.getOptions().stream()
                    .map(opt -> responseCounts.getOrDefault(opt, 0L).toString())
                    .collect(Collectors.joining(","));
            String chartConfig = String.format("{type:'bar',data:{labels:[%s],datasets:[{label:'Responses',data:[%s]}]}}", labels, data);
            return "https://quickchart.io/chart?c=" + URLEncoder.encode(chartConfig, StandardCharsets.UTF_8);
        }

        return "";
    }
}