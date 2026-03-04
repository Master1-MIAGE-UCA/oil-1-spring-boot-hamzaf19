package com.example.scoreservice.service;

import com.example.scoreservice.dto.ScoreRequest;
import com.example.scoreservice.entity.GameHistory;
import com.example.scoreservice.repository.GameHistoryRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GameHistoryService {

    private final GameHistoryRepository gameHistoryRepository;

    public GameHistoryService(GameHistoryRepository gameHistoryRepository) {
        this.gameHistoryRepository = gameHistoryRepository;
    }

    public GameHistory archiveGame(ScoreRequest request) {
        GameHistory history = GameHistory.builder()
                .playedAt(LocalDateTime.now())
                .playerId(request.playerId())
                .score(request.score())
                .build();
        return gameHistoryRepository.save(history);
    }

    public List<GameHistory> getAllHistories() {
        return gameHistoryRepository.findAll();
    }
}
