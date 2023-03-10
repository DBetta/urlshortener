package com.dbetta.urlshortener.controllers;

import com.dbetta.urlshortener.dtos.UrlShortenerRequestDto;
import com.dbetta.urlshortener.dtos.UrlShortenerResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Denis Gitonga
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlShortenerIntegrationTests {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withPassword("s3cr3t")
            .withUsername("postgres")
            .withDatabaseName("infiware-api");

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
    }

    @Test
    @DisplayName("should shorten url given long url")
    void testUrlShortening() {
        // arrange
        var now = LocalDateTime.now();
        var longUrl = "https://example.com/very/long/url";
        var urlShortenerDto = new UrlShortenerRequestDto(longUrl);
        var httpEntity = new HttpEntity<>(urlShortenerDto);

        // act
        var response = restTemplate.exchange(
                "/api/shortener",
                HttpMethod.POST,
                httpEntity,
                UrlShortenerResponseDto.class
        );

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().shortUrl()).hasSize(7);
        assertThat(response.getBody().longUrl()).isEqualTo(longUrl);
        assertThat(response.getBody().createdAt()).isAfter(now);
        assertThat(response.getBody().expiresAt()).isAfter(response.getBody().createdAt());
        assertThat(response.getBody().visits()).isEqualTo(0);

    }
}
