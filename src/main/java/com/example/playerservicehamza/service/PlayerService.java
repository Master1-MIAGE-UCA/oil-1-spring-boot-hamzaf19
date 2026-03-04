package com.example.playerservicehamza.service;

import com.example.playerservicehamza.entity.Player;
import com.example.playerservicehamza.repository.PlayerRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> findAllPlayers() {
        return playerRepository.findAll();
    }

    public Player findPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    public Player createPlayer(Player player) {
        player.setId(null);
        return playerRepository.save(player);
    }

    public Player updatePlayer(Long id, Player player) {
        Player existingPlayer = findPlayerById(id);
        existingPlayer.setPseudo(player.getPseudo());
        existingPlayer.setScore(player.getScore());
        return playerRepository.save(existingPlayer);
    }

    public void deletePlayer(Long id) {
        Player existingPlayer = findPlayerById(id);
        playerRepository.delete(existingPlayer);
    }
}
