package com.ecsimsw.auth.dto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.HashMap;
import java.util.Map;

public class RequestWrapper extends HttpServletRequestWrapper {
    private final Map<String, String> customHeaders = new HashMap<>();

    public RequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public void addHeader(String name, String value) {
        customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        return customHeaders.getOrDefault(name, super.getHeader(name));
    }

    @Override
    public java.util.Enumeration<String> getHeaderNames() {
        java.util.List<String> names = new java.util.ArrayList<>(customHeaders.keySet());
        names.addAll(java.util.Collections.list(super.getHeaderNames()));
        return java.util.Collections.enumeration(names);
    }

    @Override
    public java.util.Enumeration<String> getHeaders(String name) {
        if (customHeaders.containsKey(name)) {
            return java.util.Collections.enumeration(java.util.Collections.singletonList(customHeaders.get(name)));
        }
        return super.getHeaders(name);
    }
}

