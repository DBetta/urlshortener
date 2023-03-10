package com.dbetta.urlshortener.controllers;

import com.dbetta.urlshortener.config.SecurityConfiguration;
import com.dbetta.urlshortener.controllers.UrlShortenerController;
import com.dbetta.urlshortener.dtos.UrlShortenerResponseDto;
import com.dbetta.urlshortener.services.UrlShortenerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasLength;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Denis Gitonga
 */
@Import(SecurityConfiguration.class)
@WebMvcTest(controllers = UrlShortenerController.class,
        properties = {
                "url.shortener.url-expiry-duration=31536000s"
        }
)
public class UrlShortenerControllerTests {


    @Autowired
    private MockMvc mvc;
    @MockBean
    private UrlShortenerService urlShortenerService;

    @Test
    @DisplayName("should successfully shorten long url provided")
    void shortener_successfulShorteningUrl() throws Exception {

        var createdAt = LocalDateTime.now();
        var expiresAt = createdAt.plusMonths(3);

        given(urlShortenerService.shortener(any()))
                .willReturn(new UrlShortenerResponseDto(
                        "a123456",
                        "https://example.com/very/long/url",
                        createdAt,
                        expiresAt,
                        0
                ));

        mvc.perform(post("/api/shortener")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "longUrl": "https://example.com/very/long/url"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("shortUrl").value(hasLength(7)))
                .andExpect(jsonPath("longUrl").value("https://example.com/very/long/url"))
                .andExpect(jsonPath("createdAt").isNotEmpty())
                .andExpect(jsonPath("expiresAt").isNotEmpty())
                .andExpect(jsonPath("visits").value(0));

    }
}
