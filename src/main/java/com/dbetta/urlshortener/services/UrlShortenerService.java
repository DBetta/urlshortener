package com.dbetta.urlshortener.services;

import com.dbetta.urlshortener.config.UrlShortenerConfigurationProperties;
import com.dbetta.urlshortener.dtos.UrlShortenerRequestDto;
import com.dbetta.urlshortener.dtos.UrlShortenerResponseDto;
import com.dbetta.urlshortener.entities.UrlMapping;
import com.dbetta.urlshortener.exceptions.ShortUrlNotFoundException;
import com.dbetta.urlshortener.listeners.UrlVisitEvent;
import com.dbetta.urlshortener.repositories.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author Denis Gitonga
 */
@Service
@AllArgsConstructor
public class UrlShortenerService {

    private final Base62EncoderService encoderService;
    private final SharedCounterService sharedCounterService;
    private final UrlMappingRepository urlMappingRepository;
    private final UrlShortenerConfigurationProperties configurationProperties;
    private final ApplicationEventPublisher eventPublisher;


    public UrlShortenerResponseDto shortener(UrlShortenerRequestDto shortenerRequestDto) {

        final var counter = sharedCounterService.nextCount();
        final var shortUrl = encoderService.encode(counter);
        final var longUrl = decodeUrl(shortenerRequestDto.longUrl());
        final var expiresAt = computeUrlExpiryTime();

        // create new UrlMapping
        final var toSaveUrlMapping = UrlMapping.builder()
                .longUrl(longUrl)
                .shortUrl(shortUrl)
                .visits(0)
                .expiresAt(expiresAt)
                .build();

        // save generated url
        final var urlMapping = urlMappingRepository.save(toSaveUrlMapping);


        return new UrlShortenerResponseDto(
                urlMapping.getShortUrl(),
                urlMapping.getLongUrl(),
                urlMapping.getCreatedAt(),
                urlMapping.getExpiresAt(),
                urlMapping.getVisits()
        );
    }

    public String retrieveLongUrl(String shortUrl) {
        // TODO: check from redis
        final var urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping == null)
            throw new ShortUrlNotFoundException(String.format("%s was not found", shortUrl));

        eventPublisher.publishEvent(new UrlVisitEvent(shortUrl));
        return urlMapping.getLongUrl();
    }

    private LocalDateTime computeUrlExpiryTime() {
        final var expiryDuration = configurationProperties.getUrlExpiryDuration();
        final var now = LocalDateTime.now();

        return now.plusSeconds(expiryDuration.toSeconds());
    }

    private String decodeUrl(String longUrl) {
        return URLDecoder.decode(longUrl, StandardCharsets.UTF_8);
    }
}
