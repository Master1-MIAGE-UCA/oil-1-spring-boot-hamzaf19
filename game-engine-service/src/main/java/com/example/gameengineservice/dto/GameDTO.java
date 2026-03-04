package com.example.gameengineservice.dto;

import java.util.List;

public record GameDTO(String gameId, PlayerDTO player, List<QuestionDTO> questions) {
}
