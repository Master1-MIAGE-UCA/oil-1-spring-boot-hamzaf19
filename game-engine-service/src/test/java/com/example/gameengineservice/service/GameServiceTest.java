package com.example.gameengineservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.gameengineservice.dto.GameDTO;
import com.example.gameengineservice.dto.PlayerDTO;
import com.example.gameengineservice.dto.QuestionDTO;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private PlayerClient playerClient;

    @Mock
    private QuestionClient questionClient;

    @InjectMocks
    private GameService gameService;

    @Test
    void shouldStartNewGameSuccessfully() {
        Long playerId = 1L;
        PlayerDTO mockPlayer = new PlayerDTO(playerId, "Neo", 0);
        List<QuestionDTO> mockQuestions = List.of(new QuestionDTO(1L, "Q1", "R1"));

        when(playerClient.getPlayerById(playerId)).thenReturn(mockPlayer);
        when(questionClient.getAllQuestions()).thenReturn(mockQuestions);

        GameDTO result = gameService.startNewGame(playerId, 1);

        assertEquals("Neo", result.player().pseudo());
        assertEquals(1, result.questions().size());
        assertFalse(result.gameId().isBlank());
        verify(playerClient).getPlayerById(playerId);
        verify(questionClient).getAllQuestions();
    }

    @Test
    void shouldLimitReturnedQuestionsToRequestedNumber() {
        Long playerId = 2L;
        PlayerDTO mockPlayer = new PlayerDTO(playerId, "Trinity", 100);
        List<QuestionDTO> mockQuestions = List.of(
                new QuestionDTO(1L, "Q1", "R1"),
                new QuestionDTO(2L, "Q2", "R2"),
                new QuestionDTO(3L, "Q3", "R3")
        );

        when(playerClient.getPlayerById(playerId)).thenReturn(mockPlayer);
        when(questionClient.getAllQuestions()).thenReturn(mockQuestions);

        GameDTO result = gameService.startNewGame(playerId, 2);

        assertEquals(2, result.questions().size());
        assertEquals("Q1", result.questions().get(0).texte());
        assertEquals("Q2", result.questions().get(1).texte());
    }

    @Test
    void shouldThrowBadRequestWhenNumberOfQuestionsIsZero() {
        BadRequestException exception =
                assertThrows(BadRequestException.class, () -> gameService.startNewGame(1L, 0));

        assertTrue(exception.getMessage().contains("superieur a 0"));
        verify(playerClient, never()).getPlayerById(1L);
        verify(questionClient, never()).getAllQuestions();
    }

    @Test
    void shouldThrowGameNotFoundWhenPlayerServiceReturns404() {
        Long playerId = 404L;
        HttpClientErrorException notFoundException = HttpClientErrorException.create(
                HttpStatus.NOT_FOUND,
                "Not Found",
                HttpHeaders.EMPTY,
                new byte[0],
                StandardCharsets.UTF_8
        );

        when(playerClient.getPlayerById(playerId)).thenThrow(notFoundException);

        GameNotFoundException exception = assertThrows(
                GameNotFoundException.class,
                () -> gameService.startNewGame(playerId, 2)
        );

        assertTrue(exception.getMessage().contains("introuvable"));
        verify(questionClient, never()).getAllQuestions();
    }

    @Test
    void shouldThrowRemoteServiceExceptionWhenPlayerServiceReturnsServerError() {
        Long playerId = 1L;
        HttpServerErrorException internalError = HttpServerErrorException.create(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                HttpHeaders.EMPTY,
                new byte[0],
                StandardCharsets.UTF_8
        );

        when(playerClient.getPlayerById(playerId)).thenThrow(internalError);

        RemoteServiceException exception = assertThrows(
                RemoteServiceException.class,
                () -> gameService.startNewGame(playerId, 2)
        );

        assertTrue(exception.getMessage().contains("player-service"));
        verify(questionClient, never()).getAllQuestions();
    }

    @Test
    void shouldThrowRemoteServiceExceptionWhenQuestionServiceFails() {
        Long playerId = 1L;
        PlayerDTO mockPlayer = new PlayerDTO(playerId, "Morpheus", 500);
        HttpServerErrorException internalError = HttpServerErrorException.create(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                HttpHeaders.EMPTY,
                new byte[0],
                StandardCharsets.UTF_8
        );

        when(playerClient.getPlayerById(playerId)).thenReturn(mockPlayer);
        when(questionClient.getAllQuestions()).thenThrow(internalError);

        RemoteServiceException exception = assertThrows(
                RemoteServiceException.class,
                () -> gameService.startNewGame(playerId, 3)
        );

        assertTrue(exception.getMessage().contains("question-catalog-service"));
    }
}
