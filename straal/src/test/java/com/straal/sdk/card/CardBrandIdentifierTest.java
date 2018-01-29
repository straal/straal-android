/*
 * CardBrandIdentifierTest.java
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class CardBrandIdentifierTest {
    @ParameterizedTest
    @MethodSource({"validCardNumbers", "incompleteCardNumbers", "invalidCardNumbers"})
    void identify(String cardNumber, CardBrand brand) {
        CreditCard card = new CreditCard("JJ Abrams", cardNumber, "123", new ExpiryDate(12, 2200));
        Assertions.assertEquals(brand, CardBrandIdentifier.identify(card));
    }

    static Stream<Arguments> validCardNumbers() {
        return Stream.of(
                Arguments.of("378282246310005", CardBrand.AMEX),
                Arguments.of("371449635398431", CardBrand.AMEX),
                Arguments.of("378734493671000", CardBrand.AMEX),
                Arguments.of("379623151957810", CardBrand.AMEX),
                Arguments.of("346813438816208", CardBrand.AMEX),
                Arguments.of("341558756862475", CardBrand.AMEX),
                Arguments.of("372261702648583", CardBrand.AMEX),
                Arguments.of("346011941875148", CardBrand.AMEX),
                Arguments.of("4111111111111111", CardBrand.VISA),
                Arguments.of("4444444444444448", CardBrand.VISA),
                Arguments.of("4444333322221111", CardBrand.VISA),
                Arguments.of("4012888888881881", CardBrand.VISA),
                Arguments.of("4916589844753076", CardBrand.VISA),
                Arguments.of("4024007159750230", CardBrand.VISA),
                Arguments.of("4532371648395113", CardBrand.VISA),
                Arguments.of("4929038280596966", CardBrand.VISA),
                Arguments.of("4716448061622427", CardBrand.VISA),
                Arguments.of("6759649826438453", CardBrand.MAESTRO),
                Arguments.of("5038145356703932", CardBrand.MAESTRO),
                Arguments.of("5555555555554444", CardBrand.MASTERCARD),
                Arguments.of("5454545454545454", CardBrand.MASTERCARD),
                Arguments.of("5105105105105100", CardBrand.MASTERCARD),
                Arguments.of("5115539318330360", CardBrand.MASTERCARD),
                Arguments.of("5314186182048923", CardBrand.MASTERCARD),
                Arguments.of("5478248873641226", CardBrand.MASTERCARD),
                Arguments.of("5263948154727680", CardBrand.MASTERCARD),
                Arguments.of("5535761752128983", CardBrand.MASTERCARD)
        );
    }

    static Stream<Arguments> incompleteCardNumbers() {
        return Stream.of(
                Arguments.of("51", CardBrand.MASTERCARD),
                Arguments.of("44", CardBrand.VISA),
                Arguments.of("34", CardBrand.AMEX),
                Arguments.of("37", CardBrand.AMEX)
        );
    }

    static Stream<Arguments> invalidCardNumbers() {
        return Stream.of(
                Arguments.of("44and_nope", CardBrand.UNKNOWN),
                Arguments.of("nope", CardBrand.UNKNOWN),
                Arguments.of("", CardBrand.UNKNOWN)
        );
    }
}