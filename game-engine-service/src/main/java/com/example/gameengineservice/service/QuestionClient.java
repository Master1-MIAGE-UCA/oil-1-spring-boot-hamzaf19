package com.example.gameengineservice.service;

import com.example.gameengineservice.dto.QuestionDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class QuestionClient {

    private final RestClient.Builder restClientBuilder;
    private final DiscoveryClient discoveryClient;
    private final String serviceName;

    public QuestionClient(
            RestClient.Builder builder,
            DiscoveryClient discoveryClient,
            @Value("${services.question.name:question-catalog-service}") String serviceName
    ) {
        this.restClientBuilder = builder;
        this.discoveryClient = discoveryClient;
        this.serviceName = serviceName;
    }

    public List<QuestionDTO> getAllQuestions() {
        String baseUrl = discoveryClient.pickServiceUrl(serviceName);
        List<QuestionDTO> questions = restClientBuilder.build().get()
                .uri(baseUrl + "/api/questions")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        return questions == null ? List.of() : questions;
    }
}
