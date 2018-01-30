/*
 * CardholderNameValidatorTest.java
 * Created by Arkadiusz Różalski on 26.01.18
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

package com.straal.sdk.validation;

import com.straal.sdk.card.CreditCard;
import com.straal.sdk.card.ExpiryDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.EnumSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("CardholderNameValidator")
class CardholderNameValidatorTest {
    private CardholderNameValidator cardholderNameValidator = new CardholderNameValidator();

    @ParameterizedTest
    @MethodSource("names")
    void validate(String name, EnumSet<ValidationResult> expectedErrors) {
        CreditCard card = createCreditCard(name);
        EnumSet<ValidationResult> errors = cardholderNameValidator.validate(card);
        assertEquals(expectedErrors, errors);
    }

    private CreditCard createCreditCard(String cardholderName) {
        return new CreditCard(cardholderName, "4444444444444448", "123", new ExpiryDate(12, 2200));
    }

    static Stream<Arguments> names() {
        return Stream.of(
                Arguments.of("", EnumSet.of(ValidationResult.CARDHOLDER_NAME_TOO_SHORT)),
                Arguments.of("A", EnumSet.of(ValidationResult.CARDHOLDER_NAME_TOO_SHORT)),
                Arguments.of("A A", EnumSet.of(ValidationResult.CARDHOLDER_NAME_TOO_SHORT)),
                Arguments.of("Ab A", EnumSet.of(ValidationResult.CARDHOLDER_NAME_TOO_SHORT)),
                Arguments.of("A Ab", EnumSet.of(ValidationResult.CARDHOLDER_NAME_TOO_SHORT)),
                Arguments.of("Ab Ab", EnumSet.of(ValidationResult.VALID)),
                Arguments.of("Ab   Ab", EnumSet.of(ValidationResult.VALID)),
                Arguments.of("Jan3 Sobieski", EnumSet.of(ValidationResult.VALID))
        );
    }
}
