package com.example.playerservicehamza.service;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(Long id) {
        super("Player with id " + id + " not found.");
    }
}
