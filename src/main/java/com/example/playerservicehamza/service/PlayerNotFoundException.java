package com.example.playerservicehamza.service;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(Long id, String operation) {
        super("Joueur avec l'ID " + id + " non trouve pour l'operation: " + operation + ".");
    }
}
