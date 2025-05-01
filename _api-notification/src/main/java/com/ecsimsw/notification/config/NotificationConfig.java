package com.ecsimsw.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
public class NotificationConfig {

    public final String sender;

    public NotificationConfig(
        @Value("${spring.mail.username:ecsimsw@gmail.com}")
        String sender
    ) {
        this.sender = sender;
    }

    @Profile("!prod")
    @Bean
    public JavaMailSender javaMailSender() {
        return null;
    }
}
