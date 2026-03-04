package com.example.gameengineservice.service;

import com.example.gameengineservice.dto.PlayerDTO;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PlayerClient {

    private final RestClient.Builder restClientBuilder;
    private final DiscoveryClient discoveryClient;
    private final String serviceName;

    public PlayerClient(
            RestClient.Builder builder,
            DiscoveryClient discoveryClient,
            @Value("${services.player.name:player-service-hamza}") String serviceName
    ) {
        this.restClientBuilder = builder;
        this.discoveryClient = discoveryClient;
        this.serviceName = serviceName;
    }

    public PlayerDTO getPlayerById(Long id) {
        String baseUrl = discoveryClient.pickServiceUrl(serviceName);
        return restClientBuilder.build().get()
                .uri(baseUrl + "/api/players/{id}", id)
                .retrieve()
                .body(PlayerDTO.class);
    }

    public PlayerDTO updatePlayerScore(Long playerId, int scoreToAdd) {
        PlayerDTO existingPlayer = getPlayerById(playerId);
        int newScore = existingPlayer.score() + scoreToAdd;
        String baseUrl = discoveryClient.pickServiceUrl(serviceName);

        return restClientBuilder.build().patch()
                .uri(baseUrl + "/api/players/{id}", playerId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("score", newScore))
                .retrieve()
                .body(PlayerDTO.class);
    }
}
