package com.dbetta.urlshortener.repositories;

import com.dbetta.urlshortener.entities.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Denis Gitonga
 */
public interface UrlMappingRepository extends JpaRepository<UrlMapping, String> {
    UrlMapping findByShortUrl(String shortUrl);
}
