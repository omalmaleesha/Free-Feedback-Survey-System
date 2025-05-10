package com.edu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    private String questionText;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @ElementCollection
    private List<String> options;

    public enum QuestionType {
        TEXT, MCQ, RATING
    }
}
