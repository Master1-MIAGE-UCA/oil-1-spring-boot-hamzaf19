package com.example.scoreservice.controller;

import com.example.scoreservice.dto.ScoreRequest;
import com.example.scoreservice.entity.GameHistory;
import com.example.scoreservice.service.GameHistoryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    private final GameHistoryService gameHistoryService;

    public ScoreController(GameHistoryService gameHistoryService) {
        this.gameHistoryService = gameHistoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameHistory saveScore(@Valid @RequestBody ScoreRequest request) {
        return gameHistoryService.archiveGame(request);
    }

    @GetMapping
    public List<GameHistory> getScores() {
        return gameHistoryService.getAllHistories();
    }
}
