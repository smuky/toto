package com.muky.toto.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "firebase")
public class FirebaseProperties {

    private final Credentials credentials = new Credentials();

    public Credentials getCredentials() {
        return credentials;
    }

    public static class Credentials {
        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
