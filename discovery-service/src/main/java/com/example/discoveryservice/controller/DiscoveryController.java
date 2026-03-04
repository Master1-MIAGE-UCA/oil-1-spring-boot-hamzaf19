package com.example.discoveryservice.controller;

import com.example.discoveryservice.dto.RegistrationRequest;
import com.example.discoveryservice.service.RegistryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DiscoveryController {

    private final RegistryService registryService;

    public DiscoveryController(RegistryService registryService) {
        this.registryService = registryService;
    }

    @PostMapping("/registry")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegistrationRequest request) {
        registryService.register(request);
    }

    @GetMapping("/discovery/{serviceName}")
    public List<String> discover(@PathVariable String serviceName) {
        return registryService.discover(serviceName);
    }
}
