package com.example.scoreservice.config;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ServiceRegistrationRunner implements ApplicationListener<ApplicationReadyEvent> {

    private final RestClient restClient = RestClient.create();

    @Value("${spring.application.name}")
    private String appName;

    @Value("${server.port}")
    private String port;

    @Value("${app.discovery.url:http://localhost:8761}")
    private String discoveryUrl;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String myUrl = "http://localhost:" + port;

        try {
            restClient.post()
                    .uri(discoveryUrl + "/api/registry")
                    .body(Map.of("serviceName", appName, "url", myUrl))
                    .retrieve()
                    .toBodilessEntity();
            System.out.println("✅ Service enregistre : " + appName + " -> " + myUrl);
        } catch (Exception exception) {
            System.out.println("⚠️ Enregistrement discovery impossible pour " + appName + ": " + exception.getMessage());
        }
    }
}
