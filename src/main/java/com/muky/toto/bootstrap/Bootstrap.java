package com.muky.toto.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Bootstrap implements CommandLineRunner {

    private final MariaDbMigration mariaDbMigration;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting application bootstrap...");
        mariaDbMigration.verifyMigration();
        log.info("Bootstrap completed successfully");
    }
}
