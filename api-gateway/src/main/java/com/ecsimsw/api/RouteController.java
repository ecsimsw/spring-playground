package com.ecsimsw.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
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
    public ResponseEntity<String> routeRequest(
        @PathVariable String service,
        @RequestBody(required = false) Optional<Object> requestBody,
        HttpServletRequest request,
        HttpMethod method
    ) {
        if (!SERVICE_PORTS.containsKey(service)) {
            return Mono.just(ResponseEntity.status(404).body("Service Not Found")).block();
        }
        var url = url(service, request.getRequestURI());
        var headers = headers(request);
        return send(method, url, headers, requestBody).block();
    }

    private Mono<ResponseEntity<String>> send(HttpMethod method, String url, HashMap<String, String> headers, Optional<Object> requestBody) {
        log.info("Request url : {}", url);
        if(requestBody.isEmpty()) {
            return webClient
                .method(method)
                .uri(url)
                .headers(httpHeaders -> headers.forEach(httpHeaders::set))
                .retrieve()
                .toEntity(String.class);
        }
        return webClient
            .method(method)
            .uri(url)
            .headers(httpHeaders -> headers.forEach(httpHeaders::set))
            .bodyValue(requestBody)
            .retrieve()
            .toEntity(String.class);
    }

    private String url(String service, String uri) {
        var port = SERVICE_PORTS.get(service);
        return "http://" + HOST_NAME + ":"+ port +uri;
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
