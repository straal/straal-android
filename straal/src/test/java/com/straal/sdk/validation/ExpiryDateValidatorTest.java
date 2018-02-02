/*
 * ExpiryDateValidatorTest.java
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Calendar;
import java.util.EnumSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ExpiryDateValidator")
class ExpiryDateValidatorTest {
    private Calendar calendar = Calendar.getInstance();

    @BeforeEach
    void setup() {
        calendar.set(Calendar.YEAR, 2010);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
    }

    @ParameterizedTest
    @MethodSource({"expiryDates"})
    void validate(int month, int year, EnumSet<ValidationResult> expectedErrors) {
        CreditCard card = new CreditCard(new CardholderName("John Smith"), new CardNumber("4444444444444448"), new Cvv("123"), new ExpiryDate(month, year));
        ExpiryDateValidator expiryDateValidator = createExpiryDateValidator();
        EnumSet<ValidationResult> errors = expiryDateValidator.validate(card);
        assertEquals(expectedErrors, errors);
    }

    static Stream<Arguments> expiryDates() {
        return Stream.of(
                Arguments.of(0, 2020, EnumSet.of(ValidationResult.EXPIRY_DATE_INVALID)),
                Arguments.of(13, 2020, EnumSet.of(ValidationResult.EXPIRY_DATE_INVALID)),
                Arguments.of(10, 2000, EnumSet.of(ValidationResult.CARD_EXPIRED)),
                Arguments.of(2, 2010, EnumSet.of(ValidationResult.CARD_EXPIRED)),
                Arguments.of(0, 2010, EnumSet.of(ValidationResult.EXPIRY_DATE_INVALID, ValidationResult.CARD_EXPIRED)),
                Arguments.of(12, 2012, EnumSet.of(ValidationResult.VALID)),
                Arguments.of(3, 2010, EnumSet.of(ValidationResult.VALID))
        );
    }

    private ExpiryDateValidator createExpiryDateValidator() {
        return new ExpiryDateValidator(calendar);
    }
}
