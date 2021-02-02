/*
 * CardNumberTest.java
 * Created by Konrad Kowalewski on 26.01.18
 * Straal SDK for Android
 * Copyright 2021 Straal Sp. z o. o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
