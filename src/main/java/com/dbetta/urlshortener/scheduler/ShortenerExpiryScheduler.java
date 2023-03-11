package com.dbetta.urlshortener.scheduler;

import com.dbetta.urlshortener.entities.UrlMapping;
import com.dbetta.urlshortener.repositories.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author Denis Gitonga
 */
@Component
@AllArgsConstructor
public class ShortenerExpiryScheduler {
    private final UrlMappingRepository urlMappingRepository;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    void checkForExpiredLinks() {
        final var now = LocalDateTime.now();
        final var expiredLinks = urlMappingRepository.findAllByExpiresAtLessThanEqual(now);
        final var expiryLinkIds = expiredLinks.stream()
                .map(UrlMapping::getShortUrl)
                .toList();
        urlMappingRepository.deleteAllByIdInBatch(expiryLinkIds);
    }
}
