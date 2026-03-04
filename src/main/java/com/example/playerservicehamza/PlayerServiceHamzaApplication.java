package com.example.playerservicehamza;

import com.example.playerservicehamza.entity.Player;
import com.example.playerservicehamza.repository.PlayerRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PlayerServiceHamzaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlayerServiceHamzaApplication.class, args);
    }

    @Bean
    @ConditionalOnProperty(name = "app.seed.players.enabled", havingValue = "true", matchIfMissing = true)
    CommandLineRunner loadData(PlayerRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(Player.builder().pseudo("Neo").score(500).build());
                repository.save(Player.builder().pseudo("Trinity").score(750).build());
                repository.save(Player.builder().pseudo("Morpheus").score(1000).build());
            }
            System.out.println("-> " + repository.count() + " joueurs de test charges.");
        };
    }
}
