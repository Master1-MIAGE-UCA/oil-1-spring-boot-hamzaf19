package com.example.playerservicehamza.dto;

import jakarta.validation.constraints.Min;

public class PlayerPatchRequest {

    private String pseudo;

    @Min(value = 0, message = "Le format du score est invalide.")
    private Integer score;

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public boolean hasNoFields() {
        return pseudo == null && score == null;
    }
}
