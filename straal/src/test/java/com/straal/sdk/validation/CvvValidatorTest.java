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
    private CvvValidator cvvValidator = new CvvValidator();

    @ParameterizedTest
    @MethodSource({"cvvNumbers"})
    void validate(String cvv, EnumSet<ValidationError> expectedErrors) {
        CreditCard card = new CreditCard("John Smith", "4444444444444448", cvv, new ExpiryDate(12, 2200));
        EnumSet<ValidationError> errors = cvvValidator.validate(card);
        assertEquals(expectedErrors, errors);
    }

    static Stream<Arguments> cvvNumbers() {
        return Stream.of(
                Arguments.of("1a1", EnumSet.of(ValidationError.CVV_INVALID)),
                Arguments.of("12", EnumSet.of(ValidationError.CVV_INCOMPLETE)),
                Arguments.of("1a", EnumSet.of(ValidationError.CVV_INVALID, ValidationError.CVV_INCOMPLETE)),
                Arguments.of("123", ValidationError.emptySet())
        );
    }
}
