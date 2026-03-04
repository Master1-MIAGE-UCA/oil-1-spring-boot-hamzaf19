package com.example.gameengineservice.service;

import com.example.gameengineservice.dto.ScoreArchiveDTO;
import com.example.gameengineservice.dto.ScoreRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ScoreClient {

    private final RestClient restClient;

    public ScoreClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://localhost:8083/api/scores").build();
    }

    public ScoreArchiveDTO sendScore(Long playerId, int score) {
        return restClient.post()
                .body(new ScoreRequestDTO(playerId, score))
                .retrieve()
                .body(ScoreArchiveDTO.class);
    }
}
