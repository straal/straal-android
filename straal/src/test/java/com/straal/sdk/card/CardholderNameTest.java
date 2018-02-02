/*
 * CardholderNameTest.java
 * Created by Konrad Kowalewski on 26.01.18
 * Straal SDK for Android Tests
 * Copyright 2018 Straal Sp. z o. o.
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@DisplayName("CardholderName")
class CardholderNameTest {
    @ParameterizedTest
    @MethodSource("fullNames")
    void shouldProperlySanitizeFullNameInput(String fullName, String sanitizedFullName) {
        Assertions.assertEquals(sanitizedFullName, new CardholderName(fullName).getFullName());
    }

    @ParameterizedTest
    @MethodSource("firstAndLastNames")
    void shouldProperlySanitizePartialNameInput(String firstName, String lastName, String sanitizedFullName) {
        Assertions.assertEquals(sanitizedFullName, new CardholderName(firstName, lastName).getFullName());
    }

    static Stream<Arguments> fullNames() {
        return Stream.of(
                Arguments.of(null, ""),
                Arguments.of("", ""),
                Arguments.of("  ", ""),
                Arguments.of("Ab Cd", "Ab Cd"),
                Arguments.of("Ab\tCd", "Ab Cd"),
                Arguments.of("Ab\nCd", "Ab Cd"),
                Arguments.of("Greg O'Hare", "Greg O'Hare"),
                Arguments.of("Margarita Tequila-Tortilla", "Margarita Tequila-Tortilla"),
                Arguments.of(" Ab Cd  ", "Ab Cd"),
                Arguments.of(" 電电電 ", "電电電"),
                Arguments.of("Ab     Cd", "Ab Cd"),
                Arguments.of("  Ab     Cd   ", "Ab Cd"),
                Arguments.of("Ab Cd C", "Ab Cd C")
        );
    }

    static Stream<Arguments> firstAndLastNames() {
        return Stream.of(
                Arguments.of(null, null, ""),
                Arguments.of(null, "B", "B"),
                Arguments.of("Ab", "Cd", "Ab Cd"),
                Arguments.of("電电", "電电", "電电 電电"),
                Arguments.of("Ab Cd", "Ef", "Ab Cd Ef"),
                Arguments.of("J.", "Gonzales", "J. Gonzales"),
                Arguments.of("Ab   Cd  ", "Ef", "Ab Cd Ef"),
                Arguments.of("   Ab   ", "  Cd   ", "Ab Cd")
        );
    }
}