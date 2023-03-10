package com.dbetta.urlshortener.exceptions;

/**
 * @author Denis Gitonga
 */
public class ShortUrlNotFoundException extends RuntimeException {
    public ShortUrlNotFoundException(String message) {
        super(message);
    }
}
