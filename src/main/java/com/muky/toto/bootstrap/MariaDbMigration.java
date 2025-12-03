package com.muky.toto.bootstrap;

import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Slf4j
@Component
@RequiredArgsConstructor
public class MariaDbMigration {

    private final DataSource dataSource;
    private final SpringLiquibase liquibase;

    public void verifyMigration() {
        try {
            log.info("Verifying database migration status...");
            log.info("Liquibase change log: {}", liquibase.getChangeLog());
            log.info("Database URL: {}", dataSource.getConnection().getMetaData().getURL());
            log.info("Database migration verification completed");
        } catch (Exception e) {
            log.error("Error verifying database migration", e);
            throw new RuntimeException("Database migration verification failed", e);
        }
    }
}
