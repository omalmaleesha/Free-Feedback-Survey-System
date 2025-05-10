package com.edu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Response {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    private LocalDateTime submittedAt;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL)
    private List<Answer> answers;
}

