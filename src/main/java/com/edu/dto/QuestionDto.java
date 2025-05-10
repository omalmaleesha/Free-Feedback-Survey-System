package com.edu.dto;

import com.edu.entity.Question;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionDto {
    private String questionText;
    private Question.QuestionType type;
    private List<String> options;
}