package com.dbetta.urlshortener.services;

import com.dbetta.urlshortener.config.UrlShortenerConfigurationProperties;
import com.dbetta.urlshortener.dtos.UrlShortenerRequestDto;
import com.dbetta.urlshortener.entities.UrlMapping;
import com.dbetta.urlshortener.repositories.UrlMappingRepository;
import com.dbetta.urlshortener.services.Base62EncoderService;
import com.dbetta.urlshortener.services.SharedCounterService;
import com.dbetta.urlshortener.services.UrlShortenerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * @author Denis Gitonga
 */
@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest {

    private UrlShortenerService urlShortenerService;

    @Mock
    private Base62EncoderService encoderService;
    @Mock
    private SharedCounterService sharedCounterService;
    @Mock
    private UrlMappingRepository urlMappingRepository;
    @Mock
    private UrlShortenerConfigurationProperties configurationProperties;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @BeforeEach
    void setup() {
        urlShortenerService = new UrlShortenerService(
                encoderService,
                sharedCounterService,
                urlMappingRepository,
                configurationProperties,
                eventPublisher
        );
    }

    @Test
    void shortener_createShorterUrl() {
        var requestDto = new UrlShortenerRequestDto("https://example.com/very/long/url");
        var shortUrl = "aaaaaaa";
        var counter = 100_000_000_000L;

        var counterCaptor = ArgumentCaptor.forClass(Long.class);
        var urlMappingCaptor = ArgumentCaptor.forClass(UrlMapping.class);

        given(encoderService.encode(any())).willReturn(shortUrl);
        given(sharedCounterService.nextCount()).willReturn(counter);
        given(urlMappingRepository.save(any()))
                .willAnswer(invocation -> {
                    final var urlMapping = invocation.getArgument(0, UrlMapping.class);

                    return urlMapping.toBuilder()
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .version(1L)
                            .build();
                });
        given(configurationProperties.getUrlExpiryDuration()).willReturn(Duration.ofDays(365));

        var responseDto = urlShortenerService.shortener(requestDto);

        verify(encoderService).encode(counterCaptor.capture());
        verify(sharedCounterService).nextCount();
        verify(urlMappingRepository).save(urlMappingCaptor.capture());
        verify(configurationProperties).getUrlExpiryDuration();

        assertThat(counterCaptor.getValue()).isEqualTo(counter);


        final var toSaveUrlMapping = urlMappingCaptor.getValue();

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.shortUrl())
                .hasSize(7)
                .isEqualTo(shortUrl)
                .isEqualTo(toSaveUrlMapping.getShortUrl());
        assertThat(responseDto.longUrl())
                .isEqualTo(toSaveUrlMapping.getLongUrl());
        assertThat(responseDto.createdAt()).isNotNull();
        assertThat(responseDto.expiresAt())
                .isAfter(responseDto.createdAt())
                .isEqualTo(toSaveUrlMapping.getExpiresAt());
        assertThat(responseDto.visits())
                .isEqualTo(0)
                .isEqualTo(toSaveUrlMapping.getVisits());
    }

    @Test
    @DisplayName("should save only utf8 url")
    void shortener_saveUtf8LongUrl() {

        var requestDto = new UrlShortenerRequestDto("https%3A%2F%2Fexample.com%2Fvery%2Flong%2Furl");

        var urlMappingCaptor = ArgumentCaptor.forClass(UrlMapping.class);

        given(encoderService.encode(any())).willReturn("1232gjsd1");
        given(sharedCounterService.nextCount()).willReturn(1_000_000_000L);
        given(urlMappingRepository.save(any()))
                .willAnswer(invocation -> invocation.getArgument(0, UrlMapping.class));
        given(configurationProperties.getUrlExpiryDuration()).willReturn(Duration.ofDays(365));

        var responseDto = urlShortenerService.shortener(requestDto);

        verify(urlMappingRepository).save(urlMappingCaptor.capture());

        var urlMapping = urlMappingCaptor.getValue();

        assertThat(urlMapping.getLongUrl())
                .isEqualTo(responseDto.longUrl())
                .isEqualTo("https://example.com/very/long/url");
    }
}