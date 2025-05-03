package com.ecsimsw.gateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigTestService {

    @Value("${test.config.value}")
    private String value;

    public void print() {
        System.out.println(value);
    }
}
