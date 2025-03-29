package com.ecsimsw.gateway.service;

import com.ecsimsw.common.domain.ServiceName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.*;

@Slf4j
@Service
public class RouteService {

    private static final Map<ServiceName, List<InetSocketAddress>> SERVICE_ENDPOINTS = new HashMap<>();
    private static final Random RANDOM = new Random();

    public void register(String name, InetSocketAddress address) {
        if (SERVICE_ENDPOINTS.values().stream().anyMatch(set -> set.contains(address))) {
            throw new IllegalArgumentException("Address " + address + " already registered");
        }
        var serviceName = ServiceName.resolve(name);
        var endpoints = SERVICE_ENDPOINTS.getOrDefault(serviceName, new ArrayList<>());
        endpoints.add(address);
        SERVICE_ENDPOINTS.put(serviceName, endpoints);
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
