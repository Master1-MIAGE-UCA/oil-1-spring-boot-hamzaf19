package com.example.gameengineservice.service;

import com.example.gameengineservice.dto.GameDTO;
import com.example.gameengineservice.dto.PlayerDTO;
import com.example.gameengineservice.dto.QuestionDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

@Service
public class GameService {

    private final PlayerClient playerClient;
    private final QuestionClient questionClient;

    public GameService(PlayerClient playerClient, QuestionClient questionClient) {
        this.playerClient = playerClient;
        this.questionClient = questionClient;
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
}
