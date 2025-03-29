package com.ecsimsw.common.service;

import com.ecsimsw.common.support.ClientKeyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class InternalCommunicateService {

    private final RestTemplate restTemplate;

    @Value("${service.gateway}")
    public String gateway;

    public InternalCommunicateService(RestTemplateBuilder builder) {
        this.restTemplate = builder
            .setConnectTimeout(java.time.Duration.ofSeconds(5))
            .setReadTimeout(java.time.Duration.ofSeconds(5))
            .build();
    }

    public <T> ResponseEntity<T> request(HttpMethod method, String path, Class<T> type) {
        return request(method, path, null, type);
    }

    public <T> ResponseEntity<T> request(HttpMethod method, String path, Object requestBody, Class<T> type) {
        try {
            var url = gateway + path;
            var headers = new HttpHeaders();
            headers.set("X-Client-Key", ClientKeyUtils.init());
            var entity = new HttpEntity<>(requestBody, headers);
            return restTemplate.exchange(url, method, entity, type);
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                .headers(ex.getResponseHeaders())
                .body((T) ex.getResponseBodyAsString());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body((T) ("Internal Server Error: " + ex.getMessage()));
        }
    }
}

