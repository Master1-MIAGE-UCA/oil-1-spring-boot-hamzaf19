package com.example.gameengineservice.service;

import com.example.gameengineservice.dto.QuestionDTO;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class QuestionClient {

    private final RestClient restClient;

    public QuestionClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://localhost:8082/api/questions").build();
    }

    public List<QuestionDTO> getAllQuestions() {
        List<QuestionDTO> questions = restClient.get()
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        return questions == null ? List.of() : questions;
    }
}
