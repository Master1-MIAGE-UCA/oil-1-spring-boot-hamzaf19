package com.example.questioncatalogservice.service;

public class QuestionNotFoundException extends RuntimeException {

    public QuestionNotFoundException(Long id, String operation) {
        super("Question avec l'ID " + id + " non trouvee pour l'operation: " + operation + ".");
    }
}
