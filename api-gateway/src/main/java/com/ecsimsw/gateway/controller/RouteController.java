package com.ecsimsw.gateway.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Optional;

import static com.ecsimsw.common.config.ServiceMesh.SERVICE_PORTS;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RouteController {

    private final WebClient webClient;

    @RequestMapping("/api/{service}/**")
    public Mono<ResponseEntity<String>> routeRequest(
        @PathVariable String service,
        @RequestBody(required = false) Optional<Object> requestBody,
        HttpServletRequest request,
        HttpMethod method
    ) {
        if (!SERVICE_PORTS.containsKey(service)) {
            return Mono.just(ResponseEntity.status(404).body("Service Not Found"));
        }
        var port = SERVICE_PORTS.get(service);
        var url = "http://localhost:" + port + request.getRequestURI();
        var headers = headers(request);
        return send(method, url, headers, requestBody);
    }

    private Mono<ResponseEntity<String>> send(HttpMethod method, String url, HashMap<String, String> headers, Optional<Object> requestBody) {
        log.info("Request url : {}", url);
        return webClient
            .method(method)
            .uri(url)
            .headers(httpHeaders -> headers.forEach(httpHeaders::set))
            .bodyValue(requestBody)
            .retrieve()
            .toEntity(String.class)
            .onErrorResume(WebClientResponseException.class, exception -> Mono.just(ResponseEntity
                .status(exception.getStatusCode())
                .contentType(exception.getHeaders().getContentType() != null ? exception.getHeaders().getContentType() : MediaType.APPLICATION_JSON)
                .body(exception.getResponseBodyAsString())));
    }

    private HashMap<String, String> headers(HttpServletRequest request) {
        var headers = new HashMap<String, String>();
        var headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            var headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }
}
