package com.example.playerservicehamza.service;

import com.example.playerservicehamza.dto.PlayerPatchRequest;
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
                .orElseThrow(() -> new PlayerNotFoundException(id, "findById"));
    }

    public Player createPlayer(Player player) {
        player.setId(null);
        return playerRepository.save(player);
    }

    public Player updatePlayer(Long id, Player player) {
        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id, "update"));
        existingPlayer.setPseudo(player.getPseudo());
        existingPlayer.setScore(player.getScore());
        return playerRepository.save(existingPlayer);
    }

    public Player patchPlayer(Long id, PlayerPatchRequest request) {
        if (request.hasNoFields()) {
            throw new BadRequestException("Le corps de la requete PATCH est vide.");
        }

        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id, "patch"));

        if (request.getPseudo() != null) {
            if (request.getPseudo().isBlank()) {
                throw new BadRequestException("Le pseudo est manquant dans le corps de la requete.");
            }
            existingPlayer.setPseudo(request.getPseudo());
        }
        if (request.getScore() != null) {
            existingPlayer.setScore(request.getScore());
        }

        return playerRepository.save(existingPlayer);
    }

    public void deletePlayer(Long id) {
        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id, "delete"));
        playerRepository.delete(existingPlayer);
    }
}
