package com.ecsimsw.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ApiConfigApplication {

    public static void main(String[] args) {
        var app = new SpringApplication(ApiConfigApplication.class);
        app.run(args);
    }

}
