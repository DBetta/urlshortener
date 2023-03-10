package com.dbetta.urlshortener.listeners;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Denis Gitonga
 */
@Getter
public class UrlVisitEvent extends ApplicationEvent {
    private final String shortUrl;

    public UrlVisitEvent(String shortUrl) {
        super(shortUrl);
        this.shortUrl = shortUrl;
    }
}
