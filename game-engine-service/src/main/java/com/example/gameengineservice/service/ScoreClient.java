package com.example.gameengineservice.service;

import com.example.gameengineservice.dto.ScoreArchiveDTO;
import com.example.gameengineservice.dto.ScoreRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ScoreClient {

    private final RestClient.Builder restClientBuilder;
    private final DiscoveryClient discoveryClient;
    private final String serviceName;

    public ScoreClient(
            RestClient.Builder builder,
            DiscoveryClient discoveryClient,
            @Value("${services.score.name:score-service}") String serviceName
    ) {
        this.restClientBuilder = builder;
        this.discoveryClient = discoveryClient;
        this.serviceName = serviceName;
    }

    public ScoreArchiveDTO sendScore(Long playerId, int score) {
        String baseUrl = discoveryClient.pickServiceUrl(serviceName);
        return restClientBuilder.build().post()
                .uri(baseUrl + "/api/scores")
                .body(new ScoreRequestDTO(playerId, score))
                .retrieve()
                .body(ScoreArchiveDTO.class);
    }
}
