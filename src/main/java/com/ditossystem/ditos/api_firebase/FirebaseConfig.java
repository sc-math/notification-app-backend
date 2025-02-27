package com.ditossystem.ditos.api_firebase;

import com.google.api.client.util.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @Value("${app.firebase-configuration-file}")
    private String credentialsPath;


    public FirebaseConfig() {
    }


    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(credentialsPath).getInputStream());

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();

        if(FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        return FirebaseMessaging.getInstance();
    }
}
