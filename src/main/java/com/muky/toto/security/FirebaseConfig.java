package com.muky.toto.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Slf4j
@Configuration
@EnableConfigurationProperties(FirebaseProperties.class)
@org.springframework.context.annotation.Profile("!test")
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp(FirebaseProperties firebaseProperties, ResourceLoader resourceLoader) throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        GoogleCredentials credentials;
        String credentialsPath = firebaseProperties.getCredentials().getPath();
        
        if (credentialsPath != null && !credentialsPath.isBlank()) {
            log.info("Loading Firebase credentials from: {}", credentialsPath);
            Resource resource = resourceLoader.getResource(credentialsPath);
            if (!resource.exists()) {
                throw new IllegalStateException(
                    "Firebase credentials file not found at: " + credentialsPath + "\n" +
                    "Please download your service account JSON from Firebase Console:\n" +
                    "1. Go to Firebase Console > Project Settings > Service Accounts\n" +
                    "2. Click 'Generate New Private Key'\n" +
                    "3. Save the JSON file and set firebase.credentials.path in application.yml"
                );
            }
            credentials = GoogleCredentials.fromStream(resource.getInputStream());
            log.info("Firebase credentials loaded successfully");
        } else {
            log.warn("No firebase.credentials.path configured. Attempting to use Application Default Credentials.");
            log.warn("To configure Firebase, download service account JSON from Firebase Console and set:");
            log.warn("  firebase.credentials.path: classpath:firebase-service-account.json");
            log.warn("Or set environment variable: FIREBASE_CREDENTIALS_PATH=file:/path/to/service-account.json");
            
            try {
                credentials = GoogleCredentials.getApplicationDefault();
                log.info("Using Application Default Credentials");
            } catch (IOException e) {
                throw new IllegalStateException(
                    "Firebase credentials not found. Please configure firebase.credentials.path or set up ADC.\n" +
                    "To get your Firebase service account JSON:\n" +
                    "1. Go to https://console.firebase.google.com/\n" +
                    "2. Select your project\n" +
                    "3. Go to Project Settings (gear icon) > Service Accounts\n" +
                    "4. Click 'Generate New Private Key'\n" +
                    "5. Save the JSON file to src/main/resources/firebase-service-account.json\n" +
                    "6. Add to application.yml:\n" +
                    "   firebase:\n" +
                    "     credentials:\n" +
                    "       path: classpath:firebase-service-account.json\n" +
                    "7. Add firebase-service-account.json to .gitignore",
                    e
                );
            }
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }
}
