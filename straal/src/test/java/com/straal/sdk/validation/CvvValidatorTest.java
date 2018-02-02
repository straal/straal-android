/*
 * CvvValidatorTest.java
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

@DisplayName("CvvValidator")
class CvvValidatorTest {
    private static final String VALID_VISA_CARD_NUMBER = "4444444444444448";
    private static final String VALID_AMEX_CARD_NUMBER = "378282246310005";
    private static final String INVALID_CARD_NUMBER = "0444444444444448";
    private static final String VALID_VISA_CVV = "444";
    private static final String VALID_AMEX_CVV = "4444";
    private CvvValidator cvvValidator = new CvvValidator();

    @ParameterizedTest
    @MethodSource("creditCardData")
    void validate(String cvv, String number, EnumSet<ValidationResult> expectedErrors) {
        CreditCard card = new CreditCard("John Smith", number, cvv, new ExpiryDate(12, 2200));
        EnumSet<ValidationResult> errors = cvvValidator.validate(card);
        assertEquals(expectedErrors, errors);
    }

    static Stream<Arguments> creditCardData() {
        return Stream.of(
                Arguments.of("1a1", VALID_VISA_CARD_NUMBER, EnumSet.of(ValidationResult.CVV_INVALID)),
                Arguments.of("12", VALID_VISA_CARD_NUMBER, EnumSet.of(ValidationResult.CVV_INCOMPLETE)),
                Arguments.of("1234", VALID_VISA_CARD_NUMBER, EnumSet.of(ValidationResult.CVV_INCOMPLETE)),
                Arguments.of("1a", VALID_VISA_CARD_NUMBER, EnumSet.of(ValidationResult.CVV_INVALID, ValidationResult.CVV_INCOMPLETE)),
                Arguments.of("1a11", VALID_VISA_CARD_NUMBER, EnumSet.of(ValidationResult.CVV_INVALID, ValidationResult.CVV_INCOMPLETE)),
                Arguments.of(VALID_VISA_CVV, VALID_VISA_CARD_NUMBER, EnumSet.of(ValidationResult.VALID)),
                Arguments.of(VALID_VISA_CVV, INVALID_CARD_NUMBER, EnumSet.of(ValidationResult.CARD_PATTERN_NOT_MATCHED)),
                Arguments.of("1a11", VALID_AMEX_CARD_NUMBER, EnumSet.of(ValidationResult.CVV_INVALID)),
                Arguments.of("12", VALID_AMEX_CARD_NUMBER, EnumSet.of(ValidationResult.CVV_INCOMPLETE)),
                Arguments.of("12341", VALID_AMEX_CARD_NUMBER, EnumSet.of(ValidationResult.CVV_INCOMPLETE)),
                Arguments.of("1a", VALID_AMEX_CARD_NUMBER, EnumSet.of(ValidationResult.CVV_INVALID, ValidationResult.CVV_INCOMPLETE)),
                Arguments.of("1a111", VALID_AMEX_CARD_NUMBER, EnumSet.of(ValidationResult.CVV_INVALID, ValidationResult.CVV_INCOMPLETE)),
                Arguments.of(VALID_AMEX_CVV, VALID_AMEX_CARD_NUMBER, EnumSet.of(ValidationResult.VALID))
        );
    }
}
