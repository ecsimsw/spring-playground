package com.ecsimsw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountApplication {

    public static void main(String[] args) {
        var app = new SpringApplication(AccountApplication.class);
        app.setAdditionalProfiles("local");
        app.run(args);
    }
}