package com.dbetta.urlshortener.dtos;

import java.time.LocalDateTime;

/**
 * @author Denis Gitonga
 */
public record UrlShortenerResponseDto(
        String shortUrl,
        String longUrl,
        LocalDateTime createdAt,
        LocalDateTime expiresAt,
        int visits
) {
}
