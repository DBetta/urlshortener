package com.dbetta.urlshortener.exceptions;

/**
 * @author Denis Gitonga
 */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException() {
        super();
    }

    public InvalidInputException(String message) {
        super(message);
    }
}
