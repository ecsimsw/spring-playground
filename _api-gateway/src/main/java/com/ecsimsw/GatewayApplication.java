package com.ecsimsw;

import com.ecsimsw.gateway.service.ConfigTestService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        var app = new SpringApplication(GatewayApplication.class);
        app.run(args).getBean(ConfigTestService.class).print();
    }
}
