package com.dbetta.urlshortener.dtos;

/**
 * @author Denis Gitonga
 */
public record AuthRequestDto(
        String username,
        String password
) {
}
