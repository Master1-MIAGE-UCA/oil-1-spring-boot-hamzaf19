package com.example.discoveryservice.dto;

import jakarta.validation.constraints.NotBlank;

public record RegistrationRequest(
        @NotBlank(message = "serviceName est obligatoire.")
        String serviceName,
        @NotBlank(message = "url est obligatoire.")
        String url
) {
}
