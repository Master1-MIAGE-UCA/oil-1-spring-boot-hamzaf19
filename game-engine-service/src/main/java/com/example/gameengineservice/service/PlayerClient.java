package com.example.gameengineservice.service;

import com.example.gameengineservice.dto.PlayerDTO;
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
}
