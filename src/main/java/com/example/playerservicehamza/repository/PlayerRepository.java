package com.example.playerservicehamza.repository;

import com.example.playerservicehamza.entity.Player;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByPseudo(String pseudo);
}
