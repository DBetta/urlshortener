package com.dbetta.urlshortener.controllers;

import com.dbetta.urlshortener.config.security.JWTFilter;
import com.dbetta.urlshortener.dtos.AuthRequestDto;
import com.dbetta.urlshortener.dtos.AuthResponseDto;
import com.dbetta.urlshortener.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
@AllArgsConstructor
public class JwtController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDto> authorize(@RequestBody AuthRequestDto requestDto) {
        final var token = authenticationService.authenticate(requestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + token.token());
        return new ResponseEntity<>(token, httpHeaders, HttpStatus.OK);
    }
}
