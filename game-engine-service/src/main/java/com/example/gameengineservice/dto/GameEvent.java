package com.example.gameengineservice.dto;

public record GameEvent(
        String gameId,
        String type,
        Long playerId,
        String message,
        String timestamp
) {
}
