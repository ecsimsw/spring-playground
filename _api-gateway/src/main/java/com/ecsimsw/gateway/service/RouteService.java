package com.ecsimsw.gateway.service;

import com.ecsimsw.common.config.ServiceName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class RouteService {

    private static final ConcurrentHashMap<ServiceName, List<InetSocketAddress>> SERVICE_ENDPOINTS = new ConcurrentHashMap<>();
    private static final Random RANDOM = new Random();

    public void register(String name, InetSocketAddress address) {
        var serviceName = ServiceName.resolve(name);
        SERVICE_ENDPOINTS.computeIfAbsent(serviceName, k -> new ArrayList<>()).add(address);
        log.info("Registered service " + serviceName + " at " + address.getHostName() + ":" + address.getPort());
    }

    public void remove(String name, InetSocketAddress address) {
        var serviceName = ServiceName.resolve(name);
        if(!SERVICE_ENDPOINTS.containsKey(serviceName)) {
            log.info("Removed service " + serviceName + " at " + address.getHostName() + ":" + address.getPort());
            return;
        }
        var endpoints = SERVICE_ENDPOINTS.get(serviceName);
        endpoints.remove(address);
        SERVICE_ENDPOINTS.put(serviceName, endpoints);
        log.info("Removed service " + serviceName + " at " + address.getHostName() + ":" + address.getPort());
    }

    public InetSocketAddress getEndPoint(String serviceName) {
        return getEndPoint(ServiceName.resolve(serviceName));
    }

    public InetSocketAddress getEndPoint(ServiceName serviceName) {
        if (!SERVICE_ENDPOINTS.containsKey(serviceName)) {
            throw new IllegalArgumentException("Service " + serviceName + " not found");
        }
        var serviceEndPoints = SERVICE_ENDPOINTS.get(serviceName);
        var randomIndex = RANDOM.nextInt(serviceEndPoints.size());
        return serviceEndPoints.get(randomIndex);
    }
}
