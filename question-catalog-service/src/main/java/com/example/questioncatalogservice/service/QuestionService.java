package com.example.questioncatalogservice.service;

import com.example.questioncatalogservice.dto.QuestionPatchRequest;
import com.example.questioncatalogservice.entity.Question;
import com.example.questioncatalogservice.repository.QuestionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> findAllQuestions() {
        return questionRepository.findAll();
    }

    public Question findQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id, "findById"));
    }

    public Question createQuestion(Question question) {
        question.setId(null);
        return questionRepository.save(question);
    }

    public Question updateQuestion(Long id, Question question) {
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id, "update"));

        existingQuestion.setTexte(question.getTexte());
        existingQuestion.setReponseCorrecte(question.getReponseCorrecte());
        existingQuestion.setPropositions(question.getPropositions());
        existingQuestion.setCategorie(question.getCategorie());

        return questionRepository.save(existingQuestion);
    }

    public Question patchQuestion(Long id, QuestionPatchRequest request) {
        if (request.hasNoFields()) {
            throw new BadRequestException("Le corps de la requete PATCH est vide.");
        }

        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id, "patch"));

        if (request.getTexte() != null) {
            if (request.getTexte().isBlank()) {
                throw new BadRequestException("Le texte de la question est obligatoire.");
            }
            existingQuestion.setTexte(request.getTexte());
        }
        if (request.getReponseCorrecte() != null) {
            if (request.getReponseCorrecte().isBlank()) {
                throw new BadRequestException("La reponse correcte est obligatoire.");
            }
            existingQuestion.setReponseCorrecte(request.getReponseCorrecte());
        }
        if (request.getPropositions() != null) {
            if (request.getPropositions().isEmpty()) {
                throw new BadRequestException("La liste des propositions est obligatoire.");
            }
            existingQuestion.setPropositions(request.getPropositions());
        }
        if (request.getCategorie() != null) {
            if (request.getCategorie().isBlank()) {
                throw new BadRequestException("La categorie est obligatoire.");
            }
            existingQuestion.setCategorie(request.getCategorie());
        }

        return questionRepository.save(existingQuestion);
    }

    public void deleteQuestion(Long id) {
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id, "delete"));
        questionRepository.delete(existingQuestion);
    }
}
