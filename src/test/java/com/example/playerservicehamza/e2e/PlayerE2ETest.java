package com.example.playerservicehamza.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.playerservicehamza.entity.Player;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResponseErrorHandler;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayerE2ETest {

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }
        });
        return restTemplate;
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void shouldCreateAndRetrievePlayer() {
        String pseudo = "Alice-" + UUID.randomUUID();
        Player newPlayer = Player.builder().pseudo(pseudo).score(999).build();

        ResponseEntity<Player> createResponse = restTemplate().postForEntity(url("/api/players"), newPlayer, Player.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().getId());
        Long newId = createResponse.getBody().getId();

        ResponseEntity<Player> getResponse = restTemplate().getForEntity(url("/api/players/" + newId), Player.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(pseudo, getResponse.getBody().getPseudo());
        assertEquals(999, getResponse.getBody().getScore());
    }

    @Test
    void shouldReturnNotFoundWhenPlayerDoesNotExist() {
        ResponseEntity<String> response = restTemplate().getForEntity(url("/api/players/999999"), String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() != null && response.getBody().contains("Joueur avec l'ID"));
    }

    @Test
    void shouldPatchPlayerScore() {
        String pseudo = "PatchUser-" + UUID.randomUUID();
        Player created = restTemplate().postForEntity(
                url("/api/players"),
                Player.builder().pseudo(pseudo).score(1).build(),
                Player.class
        ).getBody();
        assertNotNull(created);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String patchBody = "{\"score\": 123}";
        HttpEntity<String> request = new HttpEntity<>(patchBody, headers);
        ResponseEntity<String> patchResponse = restTemplate().exchange(
                url("/api/players/" + created.getId()),
                HttpMethod.PATCH,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, patchResponse.getStatusCode());

        ResponseEntity<Player> refreshed = restTemplate().getForEntity(
                url("/api/players/" + created.getId()),
                Player.class
        );
        assertEquals(HttpStatus.OK, refreshed.getStatusCode());
        assertNotNull(refreshed.getBody());
        assertEquals(123, refreshed.getBody().getScore());
        assertEquals(pseudo, refreshed.getBody().getPseudo());
    }
}
