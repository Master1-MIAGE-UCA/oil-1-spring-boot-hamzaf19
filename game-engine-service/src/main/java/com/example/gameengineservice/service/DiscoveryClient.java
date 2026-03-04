package com.example.gameengineservice.service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class DiscoveryClient {

    private final RestClient restClient;

    public DiscoveryClient(
            RestClient.Builder builder,
            @Value("${discovery.url:http://localhost:8761}") String discoveryUrl
    ) {
        this.restClient = builder.baseUrl(discoveryUrl).build();
    }

    public String pickServiceUrl(String serviceName) {
        List<String> urls = restClient.get()
                .uri("/api/discovery/{serviceName}", serviceName)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (urls == null || urls.isEmpty()) {
            throw new RemoteServiceException("Aucune instance disponible pour " + serviceName + ".", null);
        }

        int index = ThreadLocalRandom.current().nextInt(urls.size());
        return urls.get(index);
    }
}
