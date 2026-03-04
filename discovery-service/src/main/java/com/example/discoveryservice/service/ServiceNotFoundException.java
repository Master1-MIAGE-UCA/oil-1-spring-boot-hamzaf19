package com.example.discoveryservice.service;

public class ServiceNotFoundException extends RuntimeException {

    public ServiceNotFoundException(String serviceName) {
        super("Service '" + serviceName + "' introuvable dans l'annuaire.");
    }
}
