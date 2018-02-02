/*
 * FullCardValidatorTest.java
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

import com.straal.sdk.card.CardNumber;
import com.straal.sdk.card.CardholderName;
import com.straal.sdk.card.CreditCard;
import com.straal.sdk.card.Cvv;
import com.straal.sdk.card.ExpiryDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.EnumSet;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

@DisplayName("FullCardValidator")
class FullCardValidatorTest {
    private static final ExpiryDate VALID_EXPIRY_DATE = new ExpiryDate(12, 2200);
    private static final CardholderName VALID_CARDHOLDER_NAME = new CardholderName("John", "Smith");
    private static final String VALID_CVV = "444";
    private FullCardValidator fullCardValidator = new FullCardValidator();

    @ParameterizedTest
    @MethodSource({"creditCardData"})
    void validate(CardholderName name, String number, String cvv, ExpiryDate expiryDate, EnumSet<ValidationResult> expectedResults) {
        CreditCard creditCard = new CreditCard(name, new CardNumber(number), new Cvv(cvv), expiryDate);
        EnumSet<ValidationResult> results = fullCardValidator.validate(creditCard);
        assertEquals(expectedResults, results);
    }

    static Stream<Arguments> creditCardData() {
        return Stream.of(
                Arguments.of(
                        VALID_CARDHOLDER_NAME,
                        "4444444444444444442",
                        VALID_CVV,
                        VALID_EXPIRY_DATE,
                        EnumSet.of(ValidationResult.VALID)
                ),
                Arguments.of(
                        VALID_CARDHOLDER_NAME,
                        "4111111111119",
                        VALID_CVV,
                        VALID_EXPIRY_DATE,
                        EnumSet.of(ValidationResult.CARD_NUMBER_INCOMPLETE, ValidationResult.VALID)
                ),
                Arguments.of(
                        VALID_CARDHOLDER_NAME,
                        "41111111111114",
                        VALID_CVV,
                        VALID_EXPIRY_DATE,
                        EnumSet.of(ValidationResult.CARD_NUMBER_INCOMPLETE)
                ),
                Arguments.of(
                        new CardholderName("Jo", "S"),
                        "4111111111119",
                        VALID_CVV,
                        VALID_EXPIRY_DATE,
                        EnumSet.of(ValidationResult.CARD_NUMBER_INCOMPLETE, ValidationResult.CARDHOLDER_NAME_INVALID)
                ),
                Arguments.of(
                        VALID_CARDHOLDER_NAME,
                        "4111111111119",
                        "44",
                        VALID_EXPIRY_DATE,
                        EnumSet.of(ValidationResult.CARD_NUMBER_INCOMPLETE, ValidationResult.CVV_INCOMPLETE)
                ),
                Arguments.of(
                        VALID_CARDHOLDER_NAME,
                        "4111111111119",
                        VALID_CVV,
                        new ExpiryDate(0, 2200),
                        EnumSet.of(ValidationResult.CARD_NUMBER_INCOMPLETE, ValidationResult.EXPIRY_DATE_INVALID)
                ),
                Arguments.of(
                        VALID_CARDHOLDER_NAME,
                        "4111111111119",
                        VALID_CVV,
                        new ExpiryDate(12, 1200),
                        EnumSet.of(ValidationResult.CARD_NUMBER_INCOMPLETE, ValidationResult.CARD_EXPIRED)
                ),
                Arguments.of(
                        VALID_CARDHOLDER_NAME,
                        "41111111111193",
                        VALID_CVV,
                        VALID_EXPIRY_DATE,
                        EnumSet.of(ValidationResult.CARD_NUMBER_INCOMPLETE, ValidationResult.LUHN_TEST_FAILED)
                ),
                Arguments.of(
                        VALID_CARDHOLDER_NAME,
                        "4111111111119a",
                        VALID_CVV,
                        VALID_EXPIRY_DATE,
                        EnumSet.of(ValidationResult.CARD_NUMBER_INCOMPLETE, ValidationResult.LUHN_TEST_FAILED, ValidationResult.CARD_NUMBER_NOT_NUMERIC)
                ),
                Arguments.of(
                        VALID_CARDHOLDER_NAME,
                        "44444444444444444481",
                        VALID_CVV,
                        VALID_EXPIRY_DATE,
                        EnumSet.of(ValidationResult.CARD_NUMBER_TOO_LONG, ValidationResult.LUHN_TEST_FAILED)
                ),
                Arguments.of(
                        VALID_CARDHOLDER_NAME,
                        "4444444444444444448a",
                        VALID_CVV,
                        VALID_EXPIRY_DATE,
                        EnumSet.of(ValidationResult.CARD_NUMBER_TOO_LONG, ValidationResult.LUHN_TEST_FAILED, ValidationResult.CARD_NUMBER_NOT_NUMERIC)
                ),
                Arguments.of(
                        new CardholderName("Jo", "S"),
                        "4444444444444s444481",
                        "a4",
                        new ExpiryDate(0, 1200),
                        EnumSet.of(
                                ValidationResult.CARD_NUMBER_NOT_NUMERIC,
                                ValidationResult.CARD_NUMBER_TOO_LONG,
                                ValidationResult.LUHN_TEST_FAILED,
                                ValidationResult.CARDHOLDER_NAME_INVALID,
                                ValidationResult.CVV_INCOMPLETE,
                                ValidationResult.CVV_INVALID,
                                ValidationResult.EXPIRY_DATE_INVALID,
                                ValidationResult.CARD_EXPIRED)
                ),
                Arguments.of(
                        VALID_CARDHOLDER_NAME,
                        "0444444444444448",
                        VALID_CVV,
                        VALID_EXPIRY_DATE,
                        EnumSet.of(ValidationResult.CARD_PATTERN_NOT_MATCHED)
                )
        );
    }
}
