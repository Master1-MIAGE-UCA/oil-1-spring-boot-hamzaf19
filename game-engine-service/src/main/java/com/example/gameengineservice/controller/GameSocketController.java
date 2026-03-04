package com.example.gameengineservice.controller;

import com.example.gameengineservice.dto.ChatMessage;
import com.example.gameengineservice.dto.GameEvent;
import java.time.LocalDateTime;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameSocketController {

    @MessageMapping("/chat/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public GameEvent handleChatMessage(@DestinationVariable String gameId, ChatMessage incomingMessage) {
        String sender = incomingMessage.sender() == null ? "Unknown" : incomingMessage.sender();
        String content = incomingMessage.content() == null ? "" : incomingMessage.content();

        return new GameEvent(
                gameId,
                "CHAT",
                null,
                sender + " dit : " + content,
                LocalDateTime.now().toString()
        );
    }
}
