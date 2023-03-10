package com.dbetta.urlshortener.controllers;

import com.dbetta.urlshortener.dtos.UrlShortenerRequestDto;
import com.dbetta.urlshortener.dtos.UrlShortenerResponseDto;
import com.dbetta.urlshortener.services.UrlShortenerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Denis Gitonga
 */
@RestController
@RequestMapping("/api")
public record UrlShortenerController(
        UrlShortenerService urlShortenerService
) {

    @PostMapping("/shortener")
    public ResponseEntity<UrlShortenerResponseDto> shortener(
            @RequestBody UrlShortenerRequestDto requestDto
    ) {
        var responseDto = urlShortenerService.shortener(requestDto);
        return ResponseEntity.ok()
                .body(responseDto);
    }
}
