package com.edu.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AnswerDto {
    private UUID questionId;
    private String value;
}