package com.dbetta.urlshortener.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Denis Gitonga
 */
@Service
public class SharedCounterService {

    private final JdbcTemplate jdbcTemplate;

    public SharedCounterService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long nextCount() {
        return jdbcTemplate.queryForObject("SELECT nextval('url_shortener.short_url_counter_seq')", Long.class);
    }
}
