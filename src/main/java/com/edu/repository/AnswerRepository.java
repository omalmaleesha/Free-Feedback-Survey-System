package com.edu.repository;

import com.edu.entity.Answer;
import com.edu.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {
    List<Answer> findByQuestion(Question question);
}
