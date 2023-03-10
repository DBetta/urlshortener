package com.dbetta.urlshortener.services;

import com.dbetta.urlshortener.exceptions.InvalidInputException;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Denis Gitonga
 */
@Component
public class Base62EncoderService {

    private static final long BASE = 62L;

    private final List<Character> lookup;

    public Base62EncoderService() {
        this.lookup = this.generateLookup().toList();
    }

    private Stream<Character> generateLookup() {
        var upperStream = IntStream.range(0, 26)
                .mapToObj(offset -> (char) (offset + 'A'));
        var lowerStream = IntStream.range(0, 26)
                .mapToObj(offset -> (char) (offset + 'a'));
        var numberStream = IntStream.range(0, 10)
                .mapToObj(offset -> (char) (offset + '0'));

        return List.of(numberStream, upperStream, lowerStream)
                .stream()
                .flatMap(charStream -> charStream);
    }

    /**
     * Encodes input to base62 from number
     *
     * @param number number to convert to base62
     * @return base62 encoded value.
     */
    public String encode(Long number) {

        if (number == null)
            throw new InvalidInputException("number should not be null");

        LinkedList<Character> list = new LinkedList<>();
        long temp = number;
        do {
            final var index = (int) (temp % BASE);
            list.addFirst(lookup.get(index));
            temp /= BASE;
        } while (temp > 0);

        return list.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    /**
     * Decodes input from base62 to long
     *
     * @param base62String encoded base62 string
     * @return corresponding number for the string passed
     */
    public long decode(String base62String) {
        if (base62String == null)
            throw new InvalidInputException("input should not be null");

        long num = 0;
        var inputLength = base62String.length();
        for (int index = 0; index < inputLength; index++) {
            int power = inputLength - (index + 1);
            int position = lookup.indexOf(base62String.charAt(index));
            if (position == -1)
                throw new InvalidInputException(
                        String.format("%s is not a valid base62 character", base62String.charAt(index))
                );
            num += position * Math.pow(BASE, power);
        }
        return num;
    }
}
