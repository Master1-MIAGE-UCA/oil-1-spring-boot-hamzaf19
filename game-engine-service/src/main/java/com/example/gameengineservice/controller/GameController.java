package com.example.gameengineservice.controller;

import com.example.gameengineservice.dto.EndGameDTO;
import com.example.gameengineservice.dto.EndGameRequest;
import com.example.gameengineservice.dto.GameDTO;
import com.example.gameengineservice.service.GameService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public GameDTO startGame(
            @PathVariable Long playerId,
            @RequestParam(defaultValue = "3") int nb,
            @RequestParam(defaultValue = "partie-default") String gameId
    ) {
        return gameService.startNewGame(playerId, nb, gameId);
    }

    @PostMapping("/end")
    public EndGameDTO endGame(@Valid @RequestBody EndGameRequest request) {
        String gameId = request.gameId() == null ? "partie-default" : request.gameId();
        return gameService.endGame(request.playerId(), request.score(), gameId);
    }

    @PostMapping("/bonus/{playerId}")
    public void sendBonus(
            @PathVariable Long playerId,
            @RequestParam String gameId,
            @RequestParam(defaultValue = "Bonus secret !") String message
    ) {
        gameService.sendPrivateMessage(gameId, playerId, message);
    }
}
