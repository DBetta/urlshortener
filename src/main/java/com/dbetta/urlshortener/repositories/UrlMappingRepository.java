package com.dbetta.urlshortener.repositories;

import com.dbetta.urlshortener.entities.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Denis Gitonga
 */
public interface UrlMappingRepository extends JpaRepository<UrlMapping, String> {
    UrlMapping findByShortUrl(String shortUrl);

    List<UrlMapping> findAllByExpiresAtLessThanEqual(LocalDateTime expiresAt);
}
