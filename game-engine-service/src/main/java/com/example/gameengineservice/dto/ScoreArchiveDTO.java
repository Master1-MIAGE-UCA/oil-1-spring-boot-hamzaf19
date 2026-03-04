package com.example.gameengineservice.dto;

import java.time.LocalDateTime;

public record ScoreArchiveDTO(Long id, LocalDateTime playedAt, Long playerId, int score) {
}
