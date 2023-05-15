package com.example.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class TokenStorage {

    private static final Logger logger = LoggerFactory.getLogger(TokenStorage.class);


    void safe(String token) throws IOException {
        final String home = System.getProperty("user.home");
        final String tokenFile = System.getenv("token_file");
        final Path tokenPath = Path.of(home, tokenFile);

        Files.writeString(tokenPath, token, UTF_8);
        logger.info("Token saved \uD83E\uDD2B");
    }
}
