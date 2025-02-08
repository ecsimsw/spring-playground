package com.ecsimsw.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class RouteController {

    private static final String HOST_NAME = "localhost";
    private static final Map<String, Integer> SERVICE_PORTS = Map.of(
        "account", 8081,
        "user", 8081,
        "auth", 8081
    );

    private final WebClient webClient;

    @RequestMapping("/api/{service}/**")
    public Mono<ResponseEntity<String>> routeRequest(
        @PathVariable String service,
        @RequestBody Object requestBody,
        HttpServletRequest request,
        HttpMethod method
    ) {
        if (!SERVICE_PORTS.containsKey(service)) {
            return Mono.just(ResponseEntity.status(404).body("Service Not Found"));
        }
        var port = SERVICE_PORTS.get(service);
        var url = "http://" + HOST_NAME + ":"+ port + request.getRequestURI();
        return webClient
            .method(method)
            .uri(url)
            .bodyValue(requestBody)
            .retrieve()
            .toEntity(String.class);
    }
}
