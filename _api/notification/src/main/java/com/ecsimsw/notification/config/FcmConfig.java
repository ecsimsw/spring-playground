package com.ecsimsw.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FcmConfig {

    @Bean
    public FirebaseApp firebaseApp() {
        try {
            var serviceAccount = getClass().getClassLoader().getResourceAsStream("secret/fcm-account.json");
            if (serviceAccount == null) {
                throw new IllegalArgumentException("Cannot find fcm-account.json");
            }
            if (FirebaseApp.getApps().isEmpty()) {
                var options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
                return FirebaseApp.initializeApp(options);
            }
            return FirebaseApp.getInstance();
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

