package com.ecsimsw.gateway.controller;

import com.ecsimsw.gateway.service.RouteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ecsimsw.common.support.MDCFilter.TRACE_ID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RouteController {

    private final WebClient webClient;
    private final RouteService routeService;

    @RequestMapping("/api/{service}/**")
    public Mono<ResponseEntity<String>> routeRequest(
        @PathVariable String service,
        @RequestBody(required = false) Optional<Object> requestBody,
        HttpServletRequest request,
        HttpMethod method
    ) {
        var endPoint = routeService.getEndPoint(service);
        var url = "http://" + endPoint.getHostName() + ":" + endPoint.getPort() + request.getRequestURI();
        var queryString = buildQueryString(request);
        if (!queryString.isBlank()) {
            url += "?" + queryString;
        }
        var headers = headers(request);
        return send(method, url, headers, requestBody);
    }

    private Mono<ResponseEntity<String>> send(HttpMethod method, String url, HashMap<String, String> headers, Optional<Object> requestBody) {
        log.info(
            "Request url : {}\n" +
            "Request headers : {}\n" +
            "Request body : {}",
            url, headers, requestBody
        );
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

    private String buildQueryString(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap(); // 쿼리 파라미터 추출
        return params.entrySet().stream()
            .map(entry -> {
                String key = entry.getKey();
                String[] values = entry.getValue();
                return Arrays.stream(values)
                    .map(value -> encodeParam(key) + "=" + encodeParam(value)) // URL 인코딩 적용
                    .collect(Collectors.joining("&")); // 같은 key의 여러 값 처리
            })
            .collect(Collectors.joining("&"));
    }

    private String encodeParam(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
