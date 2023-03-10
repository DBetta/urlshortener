package com.dbetta.urlshortener.services;

import com.dbetta.urlshortener.services.SharedCounterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * @author Denis Gitonga
 */
@ExtendWith(MockitoExtension.class)
class SharedCounterServiceTest {

    private SharedCounterService counterService;
    @Mock
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        counterService = new SharedCounterService(jdbcTemplate);
    }

    @Test
    void testGenerateNextCount() {

        var sequenceCaptor = ArgumentCaptor.forClass(String.class);

        given(jdbcTemplate.queryForObject(any(), eq(Long.class)))
                .willReturn(1_000L);

        var count = counterService.nextCount();

        verify(jdbcTemplate).queryForObject(sequenceCaptor.capture(), eq(Long.class));

        var sequence = sequenceCaptor.getValue();

        assertThat(count).isNotNull()
                .isEqualTo(1_000L);

        assertThat(sequence)
                .containsIgnoringCase("url_shortener.short_url_counter_seq")
                .isEqualToNormalizingWhitespace("SELECT nextval('url_shortener.short_url_counter_seq')");
    }
}