package com.dbetta.urlshortener.services;

import com.dbetta.urlshortener.exceptions.InvalidInputException;
import com.dbetta.urlshortener.services.Base62EncoderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Denis Gitonga
 */
public class Base62EncoderTests {

    private static Base62EncoderService encoder;

    @BeforeAll
    static void initTest() {
        encoder = new Base62EncoderService();
    }

    @ParameterizedTest
    @MethodSource("generateToBase62")
    @DisplayName("should convert input to base 62")
    void testConvertToBase62(long input, String expectedOutput) {
        String encoded = encoder.encode(input);
        assertEquals(expectedOutput, encoded);
    }

    @Test
    @DisplayName("encode should throw error when a null number is passed")
    void testToBase62NullInputs() {
        var throwable = catchThrowable(() -> encoder.encode(null));

        assertThat(throwable)
                .hasMessage("number should not be null")
                .isInstanceOf(InvalidInputException.class);
    }

    @ParameterizedTest
    @MethodSource("generateToBase62")
    @DisplayName("should convert input from base 62")
    void testConvertFromBase62(long expectedOutput, String input) {
        long decoded = encoder.decode(input);
        assertEquals(expectedOutput, decoded);
    }

    @Test
    @DisplayName("decode should throw error when a null base62 is passed")
    void testFromBase62_NullInputs() {
        var throwable = catchThrowable(() -> encoder.decode(null));

        assertThat(throwable)
                .hasMessage("input should not be null")
                .isInstanceOf(InvalidInputException.class);
    }

    @Test
    @DisplayName("decode should only accept valid base62 characters")
    void testConvertFromBase62_ValidCharacters() {
        var throwable = catchThrowable(() -> encoder.decode("@"));

        assertThat(throwable)
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("@ is not a valid base62 character");
    }


    private static Stream<Arguments> generateToBase62() {

        var numberStream = IntStream.range(0, 10)
                .mapToObj(index -> Arguments.of(index, String.valueOf((char) ('0' + index))));
        var upperStream = IntStream.range(10, 36)
                .mapToObj(index -> Arguments.of(index, String.valueOf((char) ('A' + index - 10))));
        var lowerStream = IntStream.range(36, 52)
                .mapToObj(index -> Arguments.of(index, String.valueOf((char) ('a' + index - 36))));


        var customStream = Stream.of(
                Arguments.of(62L, "10"),
                Arguments.of(1_000, "G8"),
                Arguments.of(100_000_000_000L, "1l9Zo9o"),
                Arguments.of(109_237_591_284_123L, "V1Biicwt")
        );

        return List.of(numberStream, upperStream, lowerStream, customStream)
                .stream()
                .flatMap(argumentsStream -> argumentsStream);
    }
}
