package com.example.questioncatalogservice.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le texte de la question est obligatoire.")
    @Column(nullable = false)
    private String texte;

    @NotBlank(message = "La reponse correcte est obligatoire.")
    @Column(nullable = false)
    private String reponseCorrecte;

    @ElementCollection
    @CollectionTable(name = "question_propositions", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "proposition", nullable = false)
    @NotEmpty(message = "La liste des propositions est obligatoire.")
    @Default
    private List<String> propositions = new ArrayList<>();

    @NotBlank(message = "La categorie est obligatoire.")
    @Column(nullable = false)
    private String categorie;
}
