package com.example.gameengineservice.service;

import com.example.gameengineservice.dto.EndGameDTO;
import com.example.gameengineservice.dto.GameDTO;
import com.example.gameengineservice.dto.PlayerDTO;
import com.example.gameengineservice.dto.QuestionDTO;
import com.example.gameengineservice.dto.ScoreArchiveDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

@Service
public class GameService {

    private final PlayerClient playerClient;
    private final QuestionClient questionClient;
    private final ScoreClient scoreClient;

    public GameService(PlayerClient playerClient, QuestionClient questionClient, ScoreClient scoreClient) {
        this.playerClient = playerClient;
        this.questionClient = questionClient;
        this.scoreClient = scoreClient;
    }

    public GameDTO startNewGame(Long playerId, int numberOfQuestions) {
        if (numberOfQuestions <= 0) {
            throw new BadRequestException("Le nombre de questions doit etre superieur a 0.");
        }

        PlayerDTO player;
        try {
            player = playerClient.getPlayerById(playerId);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new GameNotFoundException("Joueur avec l'ID " + playerId + " introuvable.", exception);
            }
            throw new RemoteServiceException("Erreur lors de l'appel au player-service.", exception);
        }

        List<QuestionDTO> questions;
        try {
            questions = questionClient.getAllQuestions();
        } catch (RestClientResponseException exception) {
            throw new RemoteServiceException("Erreur lors de l'appel au question-catalog-service.", exception);
        }

        List<QuestionDTO> selectedQuestions = questions.stream()
                .limit(numberOfQuestions)
                .toList();

        return new GameDTO(UUID.randomUUID().toString(), player, selectedQuestions);
    }

    public EndGameDTO endGame(Long playerId, int score) {
        if (score < 0) {
            throw new BadRequestException("Le score doit etre superieur ou egal a 0.");
        }

        ScoreArchiveDTO archivedGame;
        try {
            archivedGame = scoreClient.sendScore(playerId, score);
        } catch (RestClientResponseException exception) {
            throw new RemoteServiceException("Erreur lors de l'appel au score-service.", exception);
        }

        PlayerDTO updatedPlayer;
        try {
            updatedPlayer = playerClient.updatePlayerScore(playerId, score);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new GameNotFoundException("Joueur avec l'ID " + playerId + " introuvable.", exception);
            }
            throw new RemoteServiceException("Erreur lors de la mise a jour du joueur.", exception);
        }

        return new EndGameDTO(
                playerId,
                score,
                updatedPlayer.score(),
                archivedGame.id(),
                archivedGame.playedAt()
        );
    }
}
