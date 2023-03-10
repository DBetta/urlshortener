package com.dbetta.urlshortener.services;

import com.dbetta.urlshortener.dtos.AuthRequestDto;
import com.dbetta.urlshortener.dtos.AuthResponseDto;
import com.dbetta.urlshortener.dtos.RegisterRequestDto;
import com.dbetta.urlshortener.dtos.RegisterResponseDto;
import com.dbetta.urlshortener.entities.Role;
import com.dbetta.urlshortener.entities.User;
import com.dbetta.urlshortener.exceptions.UserAlreadyExistingException;
import com.dbetta.urlshortener.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Denis Gitonga
 */
@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtService jwtService;

    public RegisterResponseDto registerUser(RegisterRequestDto requestDto) {
        validateUserDoesNotExist(requestDto);

        final var encodedPassword = passwordEncoder.encode(requestDto.password());

        final var toSaveUser = User.builder()
                .email(requestDto.email())
                .firstname(requestDto.firstName())
                .lastname(requestDto.lastName())
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        final var user = userRepository.save(toSaveUser);
        return new RegisterResponseDto(
                user.getId(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname()
        );
    }

    public AuthResponseDto authenticate(AuthRequestDto requestDto) {
        final var usernameToken = new UsernamePasswordAuthenticationToken(
                requestDto.username(),
                requestDto.password()
        );
        final var authentication = authenticationManagerBuilder.getObject()
                .authenticate(usernameToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final var token = jwtService.generateToken(authentication);
        return new AuthResponseDto(token);
    }

    private void validateUserDoesNotExist(RegisterRequestDto requestDto) {
        var optionalUser = userRepository.findByEmail(requestDto.email());

        if (optionalUser.isPresent())
            throw new UserAlreadyExistingException();
    }
}
