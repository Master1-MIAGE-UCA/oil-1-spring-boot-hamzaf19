package com.example.questioncatalogservice.dto;

import java.util.List;

public class QuestionPatchRequest {

    private String texte;
    private String reponseCorrecte;
    private List<String> propositions;
    private String categorie;

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public String getReponseCorrecte() {
        return reponseCorrecte;
    }

    public void setReponseCorrecte(String reponseCorrecte) {
        this.reponseCorrecte = reponseCorrecte;
    }

    public List<String> getPropositions() {
        return propositions;
    }

    public void setPropositions(List<String> propositions) {
        this.propositions = propositions;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public boolean hasNoFields() {
        return texte == null && reponseCorrecte == null && propositions == null && categorie == null;
    }
}
