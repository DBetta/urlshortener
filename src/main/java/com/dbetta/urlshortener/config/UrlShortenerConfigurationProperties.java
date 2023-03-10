package com.dbetta.urlshortener.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author Denis Gitonga
 */
@AllArgsConstructor
@ConfigurationProperties("url.shortener")
@Getter
public class UrlShortenerConfigurationProperties {
    private final Duration urlExpiryDuration;
    private final Duration jwtTokenExpiry;
    private final String jwtBase64Secret;
}
