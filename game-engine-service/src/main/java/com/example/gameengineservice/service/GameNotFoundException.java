package com.example.gameengineservice.service;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
