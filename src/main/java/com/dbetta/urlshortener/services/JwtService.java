package com.dbetta.urlshortener.services;

import com.dbetta.urlshortener.config.UrlShortenerConfigurationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * @author Denis Gitonga
 */
@Service
@AllArgsConstructor
public class JwtService {

    private static final String AUTHORITIES_KEY = "auth";
    private final UrlShortenerConfigurationProperties properties;

    public String generateToken(Authentication authentication) {
        final var expiryMillis = properties.getJwtTokenExpiry().toMillis();
        final var authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .claim(AUTHORITIES_KEY, authorities)
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryMillis))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        final var jwtParser = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build();
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        final var jwtParser = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build();
        try {
            jwtParser.parseClaimsJws(authToken);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getJwtBase64Secret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
