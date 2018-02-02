package com.straal.sdk.card;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@DisplayName("CardNumber")
class CardNumberTest {
    @ParameterizedTest
    @MethodSource("numbers")
    void shouldReturnProperlySanitizedValue(String number, String sanitizedNumber) {
        Assertions.assertEquals(sanitizedNumber, new CardNumber(number).sanitized());
    }

    static Stream<Arguments> numbers() {
        return Stream.of(
                Arguments.of("4111--1111  1111\t1111", "4111111111111111"),
                Arguments.of("4111-1111-1111-1111", "4111111111111111"),
                Arguments.of("4111 1111 1111 1111", "4111111111111111"),
                Arguments.of("4111 1111 abc 1111", "41111111abc1111"),
                Arguments.of("  4111111111111111  ", "4111111111111111"),
                Arguments.of("4111111111111111", "4111111111111111")
        );
    }
}