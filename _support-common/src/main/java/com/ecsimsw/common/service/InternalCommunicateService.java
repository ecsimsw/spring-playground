package com.ecsimsw.common.service;

import com.ecsimsw.common.support.utils.ClientKeyUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static com.ecsimsw.common.config.LogConfig.MDC_TRACE_ID;
import static com.ecsimsw.common.config.LogConfig.TRACE_ID_HEADER;

@Service
public class InternalCommunicateService {

    private final RestTemplate restTemplate;
    private final WebClient webClient;

    @Value("${service.gateway:}")
    public String gateway;

    public InternalCommunicateService(RestTemplateBuilder builder, WebClient webClient) {
        this.webClient = webClient;
        this.restTemplate = builder
            .setConnectTimeout(java.time.Duration.ofSeconds(5))
            .setReadTimeout(java.time.Duration.ofSeconds(5))
            .build();
    }

    public <T> ResponseEntity<T> request(HttpMethod method, String path, Class<T> type) {
        return request(method, path, null, type);
    }

    public <T> Mono<ResponseEntity<T>> requestAsync(HttpMethod method, String path, Class<T> type) {
        return requestAsync(method, path, null, type);
    }

    public <T> Mono<ResponseEntity<T>> requestAsync(HttpMethod method, String path, Object requestBody, Class<T> type) {
        var url = gateway + path;
        var headers = getHeaders();
        if(requestBody == null) {
            return webClient.method(method)
                .uri(url)
                .headers(httpHeaders -> headers.forEach(httpHeaders::set))
                .retrieve()
                .toEntity(type);
        }
        return webClient.method(method)
            .uri(url)
            .headers(httpHeaders -> headers.forEach(httpHeaders::set))
            .bodyValue(requestBody)
            .retrieve()
            .toEntity(type);
    }

    public <T> ResponseEntity<T> request(HttpMethod method, String path, Object requestBody, Class<T> type) {
        try {
            var url = gateway + path;
            return restTemplate.exchange(url, method, httpEntity(requestBody), type);
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                .headers(ex.getResponseHeaders())
                .body((T) ex.getResponseBodyAsString());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body((T) ("Internal Server Error: " + ex.getMessage()));
        }
    }

    private HttpEntity<Object> httpEntity(Object requestBody) {
        var httpHeaders = new HttpHeaders();
        var headers = getHeaders();
        for (var h : headers.keySet()) {
            httpHeaders.set(h, headers.get(h));
        }
        if (requestBody != null) {
            return new HttpEntity<>(requestBody, httpHeaders);
        }
        return new HttpEntity<>(httpHeaders);
    }

    private Map<String, String> getHeaders() {
        var headers = new HashMap<String, String>();
        headers.put(TRACE_ID_HEADER, MDC.get(MDC_TRACE_ID));
        headers.put("X-Client-Key", ClientKeyUtils.init());
        headers.put(HttpHeaders.CONTENT_TYPE, "application/json");
        return headers;
    }
}

