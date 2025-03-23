package com.ecsimsw.common.service;

import com.ecsimsw.common.support.ClientKeyUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.ecsimsw.common.config.ServiceMesh.SERVICE_PORTS;

@Service
public class InternalCommunicateService {

    private final RestTemplate restTemplate;

    public InternalCommunicateService(RestTemplateBuilder builder) {
        this.restTemplate = builder
            .setConnectTimeout(java.time.Duration.ofSeconds(5))
            .setReadTimeout(java.time.Duration.ofSeconds(5))
            .build();
    }

    public <T> ResponseEntity<T> request(HttpMethod method, String path, Object requestBody, Class<T> type) {
        var serviceName = parseServiceName(path);
        if (!SERVICE_PORTS.containsKey(serviceName)) {
            throw new IllegalArgumentException("Service " + serviceName + " not found");
        }
        var port = SERVICE_PORTS.get(serviceName);
        var url = "http://localhost:" + port + path;
        var headers = new HttpHeaders();
        headers.set("X-Client-Key", ClientKeyUtils.init());

        var entity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(url, method, entity, type);
    }

    private String parseServiceName(String path) {
        var indexFrom = path.indexOf("api/") + "api/".length();
        var indexTo = path.indexOf("/", indexFrom);
        return path.substring(indexFrom, indexTo);
    }
}

