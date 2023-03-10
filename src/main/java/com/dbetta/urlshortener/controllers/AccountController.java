package com.dbetta.urlshortener.controllers;

import com.dbetta.urlshortener.dtos.RegisterRequestDto;
import com.dbetta.urlshortener.dtos.RegisterResponseDto;
import com.dbetta.urlshortener.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Denis Gitonga
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AccountController {

    private AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegisterResponseDto> registerAccount(@RequestBody RegisterRequestDto registerRequestDto) {
        final var response = authenticationService.registerUser(registerRequestDto);
        return ResponseEntity.ok(response);
    }
}
