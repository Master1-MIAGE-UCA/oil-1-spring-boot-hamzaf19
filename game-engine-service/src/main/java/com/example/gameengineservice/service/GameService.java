package com.example.gameengineservice.service;

import com.example.gameengineservice.dto.EndGameDTO;
import com.example.gameengineservice.dto.GameDTO;
import com.example.gameengineservice.dto.GameEvent;
import com.example.gameengineservice.dto.PlayerDTO;
import com.example.gameengineservice.dto.QuestionDTO;
import com.example.gameengineservice.dto.ScoreArchiveDTO;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

@Service
public class GameService {

    private final PlayerClient playerClient;
    private final QuestionClient questionClient;
    private final ScoreClient scoreClient;
    private final SimpMessagingTemplate messagingTemplate;

    public GameService(
            PlayerClient playerClient,
            QuestionClient questionClient,
            ScoreClient scoreClient,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.playerClient = playerClient;
        this.questionClient = questionClient;
        this.scoreClient = scoreClient;
        this.messagingTemplate = messagingTemplate;
    }

    public GameDTO startNewGame(Long playerId, int numberOfQuestions, String gameId) {
        if (numberOfQuestions <= 0) {
            throw new BadRequestException("Le nombre de questions doit etre superieur a 0.");
        }
        if (gameId == null || gameId.isBlank()) {
            throw new BadRequestException("Le gameId est obligatoire.");
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

        GameDTO game = new GameDTO(gameId, player, selectedQuestions);
        notifyGameUpdate(
                gameId,
                new GameEvent(
                        gameId,
                        "GAME_STARTED",
                        playerId,
                        "Partie demarree pour " + player.pseudo() + " avec " + selectedQuestions.size() + " questions.",
                        LocalDateTime.now().toString()
                )
        );
        return game;
    }

    public EndGameDTO endGame(Long playerId, int score, String gameId) {
        if (score < 0) {
            throw new BadRequestException("Le score doit etre superieur ou egal a 0.");
        }
        if (gameId == null || gameId.isBlank()) {
            throw new BadRequestException("Le gameId est obligatoire.");
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

        EndGameDTO endGame = new EndGameDTO(
                playerId,
                score,
                updatedPlayer.score(),
                archivedGame.id(),
                archivedGame.playedAt()
        );
        notifyGameUpdate(
                gameId,
                new GameEvent(
                        gameId,
                        "GAME_ENDED",
                        playerId,
                        "Partie terminee. +" + score + " points.",
                        LocalDateTime.now().toString()
                )
        );
        return endGame;
    }

    public void notifyGameUpdate(String gameId, GameEvent event) {
        String destination = "/topic/game/" + gameId;
        messagingTemplate.convertAndSend(destination, event);
    }

    public void sendPrivateMessage(String gameId, Long playerId, String secretInfo) {
        String destination = "/topic/player/" + playerId;
        GameEvent secretEvent = new GameEvent(
                gameId,
                "SECRET",
                playerId,
                secretInfo,
                LocalDateTime.now().toString()
        );
        messagingTemplate.convertAndSend(destination, secretEvent);
    }
}
