package com.ecsimsw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiEventApplication {

    public static void main(String[] args) {
        var app = new SpringApplication(ApiEventApplication.class);
        app.run(args);
    }

}
