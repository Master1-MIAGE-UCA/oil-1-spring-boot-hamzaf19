package com.example.gameengineservice.service;

import com.example.gameengineservice.dto.PlayerDTO;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PlayerClient {

    private final RestClient restClient;

    public PlayerClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://localhost:8081/api/players").build();
    }

    public PlayerDTO getPlayerById(Long id) {
        return restClient.get()
                .uri("/{id}", id)
                .retrieve()
                .body(PlayerDTO.class);
    }

    public PlayerDTO updatePlayerScore(Long playerId, int scoreToAdd) {
        PlayerDTO existingPlayer = getPlayerById(playerId);
        int newScore = existingPlayer.score() + scoreToAdd;

        return restClient.patch()
                .uri("/{id}", playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("score", newScore))
                .retrieve()
                .body(PlayerDTO.class);
    }
}
