package com.example.gameengineservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EndGameRequest(
        @NotNull(message = "playerId est obligatoire.")
        Long playerId,
        @Min(value = 0, message = "Le score doit etre superieur ou egal a 0.")
        int score,
        String gameId
) {
}
