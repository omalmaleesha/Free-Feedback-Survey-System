package com.edu.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SurveyDto {
    private UUID id;
    private String title;
    private String description;
    private UUID publicUuid;
    private UUID adminToken;
    private List<QuestionDto> questions;
}
