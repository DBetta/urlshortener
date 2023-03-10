package com.dbetta.urlshortener.config;

import com.dbetta.urlshortener.config.security.JWTConfigurer;
import com.dbetta.urlshortener.services.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Denis Gitonga
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private final JwtService jwtService;

    @Bean
    SecurityFilterChain securityFilterChain(@NonNull HttpSecurity http) throws Exception {
        http
                .sessionManagement(configurer -> configurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/l/**").permitAll()
                        .requestMatchers("/api/authenticate").permitAll()
                        .requestMatchers("/api/register").permitAll()
                        .anyRequest().authenticated())
                .httpBasic()
                .and()
                .apply(new JWTConfigurer(jwtService));
        return http
                .csrf().disable()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
