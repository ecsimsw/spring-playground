package com.ecsimsw.device.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FcmConfig {

    @Bean
    public FirebaseApp firebaseApp() {
        try {
            var resource = new ClassPathResource("secret/fcm-account.json");
            if (!resource.exists()) {
                throw new IllegalArgumentException("Cannot find fcm-account.json");
            }
            try (var serviceAccount = resource.getInputStream()) {
                if (FirebaseApp.getApps().isEmpty()) {
                    var options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                    return FirebaseApp.initializeApp(options);
                }
                return FirebaseApp.getInstance();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp app) {
        return FirebaseMessaging.getInstance(app);
    }
}

