package com.example.gameengineservice.dto;

import java.time.LocalDateTime;

public record EndGameDTO(
        Long playerId,
        int gainedScore,
        int totalScore,
        Long archivedGameId,
        LocalDateTime archivedAt
) {
}
