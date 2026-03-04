package com.example.playerservicehamza.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class PlayerPatchRequest {

    @NotBlank(message = "Le pseudo est vide.")
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
