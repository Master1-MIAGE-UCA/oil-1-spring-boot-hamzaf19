package com.example.questioncatalogservice;

import com.example.questioncatalogservice.entity.Question;
import com.example.questioncatalogservice.repository.QuestionRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QuestionCatalogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuestionCatalogServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner loadData(QuestionRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(Question.builder()
                        .texte("Quelle est la version LTS de Java demandee pour le projet ?")
                        .reponseCorrecte("Java 21")
                        .propositions(List.of("Java 8", "Java 11", "Java 17", "Java 21"))
                        .categorie("Java")
                        .build());
                repository.save(Question.builder()
                        .texte("Quel composant Spring expose des endpoints REST ?")
                        .reponseCorrecte("Controller")
                        .propositions(List.of("Entity", "Controller", "Repository", "Bean"))
                        .categorie("Spring")
                        .build());
                repository.save(Question.builder()
                        .texte("Quel code HTTP correspond a une creation reussie ?")
                        .reponseCorrecte("201")
                        .propositions(List.of("200", "201", "204", "404"))
                        .categorie("HTTP")
                        .build());
                repository.save(Question.builder()
                        .texte("Quelle annotation gere les entites persistantes ?")
                        .reponseCorrecte("@Entity")
                        .propositions(List.of("@Service", "@Entity", "@Bean", "@Repository"))
                        .categorie("JPA")
                        .build());
            }
            System.out.println("-> " + repository.count() + " questions de test chargees.");
        };
    }
}
