package com.example.gameengineservice.controller;

import com.example.gameengineservice.dto.GameDTO;
import com.example.gameengineservice.service.GameService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start/{playerId}")
    public GameDTO startGame(@PathVariable Long playerId, @RequestParam(defaultValue = "3") int nb) {
        return gameService.startNewGame(playerId, nb);
    }
}
