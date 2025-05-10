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
public class Survey {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String title;

    private String description;

    @Column(unique = true)
    private UUID publicUuid;

    @Column(unique = true)
    private UUID adminToken;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
    private List<Question> questions;
}

