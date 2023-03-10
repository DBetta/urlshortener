package com.dbetta.urlshortener.dtos;

/**
 * @author Denis Gitonga
 */
public record RegisterRequestDto(
        String email,
        String password,
        String firstName,
        String lastName
) {
}
