package com.ecsimsw.gateway.controller;

import com.ecsimsw.gateway.service.RouteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
@RestController
public class ServiceMeshController {

    private final RouteService routeService;

    @PostMapping("/api/gateway/service")
    public ResponseEntity<Void> register(
        @RequestParam String service,
        @RequestParam int port,
        HttpServletRequest request
    ) {
        var endPoint = InetSocketAddress.createUnresolved(request.getRemoteAddr(), port);
        routeService.register(service, endPoint);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/gateway/service")
    public ResponseEntity<Void> remove(
        @RequestParam String service,
        @RequestParam int port,
        HttpServletRequest request
    ) {
        var endPoint = InetSocketAddress.createUnresolved(request.getRemoteAddr(), port);
        routeService.remove(service, endPoint);
        return ResponseEntity.ok().build();
    }
}
