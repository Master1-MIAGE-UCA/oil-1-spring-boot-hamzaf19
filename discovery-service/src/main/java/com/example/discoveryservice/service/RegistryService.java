package com.example.discoveryservice.service;

import com.example.discoveryservice.dto.RegistrationRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Service;

@Service
public class RegistryService {

    private final Map<String, List<String>> registry = new ConcurrentHashMap<>();

    public void register(RegistrationRequest request) {
        registry.compute(request.serviceName(), (name, urls) -> {
            List<String> values = urls == null ? new CopyOnWriteArrayList<>() : urls;
            if (!values.contains(request.url())) {
                values.add(request.url());
            }
            return values;
        });
    }

    public List<String> discover(String serviceName) {
        List<String> urls = registry.get(serviceName);
        if (urls == null || urls.isEmpty()) {
            throw new ServiceNotFoundException(serviceName);
        }
        return urls;
    }
}
