package com.example.Investigation_Tracking_Solution.config;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleanupConfig {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseCleanupConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void cleanup() {
        try {
            jdbcTemplate.execute("ALTER TABLE criminals DROP COLUMN date_of_birth");
            System.out.println("✅ Automatically dropped redundant 'date_of_birth' column from DB.");
        } catch (Exception e) {
            // Ignore if column doesn't exist
        }
        
        try {
            jdbcTemplate.execute("ALTER TABLE criminals DROP COLUMN criminal_status");
            System.out.println("✅ Automatically dropped redundant 'criminal_status' column from DB.");
        } catch (Exception e) {
            // Ignore if column doesn't exist
        }
    }
}
