package com.ecsimsw.common.config;

import java.util.Map;

public class ServiceMesh {
    
    public static final Map<String, Integer> SERVICE_PORTS = Map.of(
        "user", 8081,
        "auth", 8082
    );
}
