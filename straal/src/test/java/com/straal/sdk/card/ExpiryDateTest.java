/*
 * ExpiryDateTest.java
 * Created by Konrad Kowalewski on 26.01.18
 * Straal SDK for Android Tests
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
 */

package com.straal.sdk.card;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ExpiryDate")
class ExpiryDateTest {
    private static final int VALID_MONTH = 1;
    private static final String VALID_MONTH_AS_STRING = "1";
    private static final String VALID_YEAR_AS_STRING = "2010";

    @ParameterizedTest
    @MethodSource("expiryYearData")
    void validateYear(int year, int expectedYear) {
        ExpiryDate expiryDate = new ExpiryDate(VALID_MONTH, year);
        assertEquals(expectedYear, expiryDate.year);
    }

    static Stream<Arguments> expiryYearData() {
        return Stream.of(
                Arguments.of(0, 2000),
                Arguments.of(99, 2099),
                Arguments.of(2002, 2002),
                Arguments.of(100, 100),
                Arguments.of(1000, 1000)
        );
    }

    @ParameterizedTest
    @MethodSource("expiryYearAsStringData")
    void validateYearAsString(String year, int expectedYear) {
        ExpiryDate expiryDate = new ExpiryDate(VALID_MONTH_AS_STRING, year);
        assertEquals(expectedYear, expiryDate.year);
    }

    static Stream<Arguments> expiryYearAsStringData() {
        return Stream.of(
                Arguments.of("0", 2000),
                Arguments.of("99", 2099),
                Arguments.of("2002", 2002),
                Arguments.of("100", 100),
                Arguments.of("1000", 1000),
                Arguments.of("01000", 1000),
                Arguments.of("a", 2000),
                Arguments.of("a1", 2000)
        );
    }

    @ParameterizedTest
    @MethodSource("expiryMonthAsStringData")
    void validateMonthAsString(String month, int expectedMonth) {
        ExpiryDate expiryDate = new ExpiryDate(month, VALID_YEAR_AS_STRING);
        assertEquals(expectedMonth, expiryDate.month);
    }

    static Stream<Arguments> expiryMonthAsStringData() {
        return Stream.of(
                Arguments.of("3", 3),
                Arguments.of("03", 3),
                Arguments.of("a", 0)
        );
    }
}
