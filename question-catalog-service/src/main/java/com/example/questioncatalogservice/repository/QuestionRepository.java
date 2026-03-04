package com.example.questioncatalogservice.repository;

import com.example.questioncatalogservice.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
