package com.dbetta.urlshortener.listeners;

import com.dbetta.urlshortener.repositories.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Denis Gitonga
 */
@Component
@AllArgsConstructor
public class UrlVisitEventListener implements ApplicationListener<UrlVisitEvent> {

    private final UrlMappingRepository urlMappingRepository;

    @Override
    public void onApplicationEvent(UrlVisitEvent event) {
        // get url mapping
        final var urlMapping = urlMappingRepository.findByShortUrl(event.getShortUrl());
        if (urlMapping == null) return;

        urlMappingRepository.save(urlMapping.toBuilder()
                .visits(urlMapping.getVisits() + 1)
                .build());
    }
}
