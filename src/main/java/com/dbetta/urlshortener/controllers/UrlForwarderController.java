package com.dbetta.urlshortener.controllers;

import com.dbetta.urlshortener.services.UrlShortenerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

/**
 * @author Denis Gitonga
 */
@Controller
@AllArgsConstructor
@RequestMapping("/l")
public class UrlForwarderController {

    private UrlShortenerService urlShortenerService;

    @GetMapping("/{shortUrl}")
    ResponseEntity<Object> forward(@PathVariable("shortUrl") String shortUrl) {
        try {
            final var longUrl = urlShortenerService.retrieveLongUrl(shortUrl);
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                    .location(URI.create(longUrl))
                    .build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }
}
