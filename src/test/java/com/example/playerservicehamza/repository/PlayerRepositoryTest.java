package com.example.playerservicehamza.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.playerservicehamza.entity.Player;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest(properties = "app.seed.players.enabled=false")
class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void shouldFindPlayerByPseudo() {
        Player player = Player.builder().pseudo("Trinity").score(10).build();
        playerRepository.save(player);

        Optional<Player> found = playerRepository.findByPseudo("Trinity");

        assertTrue(found.isPresent());
        assertEquals(10, found.get().getScore());
    }

    @Test
    void shouldSavePlayer() {
        Player newPlayer = Player.builder().pseudo("Morpheus").build();
        Player saved = playerRepository.save(newPlayer);

        assertNotNull(saved.getId());
        assertEquals(0, saved.getScore());
    }

    @Test
    void shouldReturnEmptyWhenPseudoNotFound() {
        Optional<Player> found = playerRepository.findByPseudo("UnknownPseudo");

        assertTrue(found.isEmpty());
    }

    @Test
    void shouldRejectDuplicatePseudo() {
        playerRepository.save(Player.builder().pseudo("Neo").score(5).build());

        assertThrows(DataIntegrityViolationException.class, () -> {
            playerRepository.saveAndFlush(Player.builder().pseudo("Neo").score(9).build());
        });
    }
}
