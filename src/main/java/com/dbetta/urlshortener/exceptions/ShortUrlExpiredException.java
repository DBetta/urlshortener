package com.dbetta.urlshortener.exceptions;

/**
 * @author Denis Gitonga
 */
public class ShortUrlExpiredException extends RuntimeException {
    public ShortUrlExpiredException(String message) {
        super(message);
    }
}
