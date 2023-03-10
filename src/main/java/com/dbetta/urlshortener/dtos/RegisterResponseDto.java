package com.dbetta.urlshortener.dtos;

import java.util.UUID;

/**
 * @author Denis Gitonga
 */
public record RegisterResponseDto(
        UUID id,
        String email,
        String firstName,
        String lastName
) {

}
