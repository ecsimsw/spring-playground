package com.ecsimsw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventApplication {

    public static void main(String[] args) {
        var app = new SpringApplication(EventApplication.class);
        app.run(args);
    }

}
