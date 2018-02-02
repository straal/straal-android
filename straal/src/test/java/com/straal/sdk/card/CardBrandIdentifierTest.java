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
                Arguments.of("4175000398358607", CardBrand.VISA_ELECTRON),
                Arguments.of("4844729377511403", CardBrand.VISA_ELECTRON),
                Arguments.of("4508973186169659", CardBrand.VISA_ELECTRON),
                Arguments.of("4917221508110521", CardBrand.VISA_ELECTRON),
                Arguments.of("4844064835532001", CardBrand.VISA_ELECTRON),
                Arguments.of("4913711310420948", CardBrand.VISA_ELECTRON),
                Arguments.of("6759649826438453", CardBrand.SWITCH),
                Arguments.of("6759649826438453", CardBrand.SWITCH),
                Arguments.of("4111111111111111", CardBrand.VISA),
                Arguments.of("4444444444444448", CardBrand.VISA),
                Arguments.of("4444333322221111", CardBrand.VISA),
                Arguments.of("4012888888881881", CardBrand.VISA),
                Arguments.of("4916589844753076", CardBrand.VISA),
                Arguments.of("4024007159750230", CardBrand.VISA),
                Arguments.of("4532371648395113", CardBrand.VISA),
                Arguments.of("4929038280596966", CardBrand.VISA),
                Arguments.of("4716448061622427", CardBrand.VISA),
                Arguments.of("4024007197789", CardBrand.VISA),
                Arguments.of("4929997303280", CardBrand.VISA),
                Arguments.of("4485396672874", CardBrand.VISA),
                Arguments.of("4640672490785", CardBrand.VISA),
                Arguments.of("4485289976572", CardBrand.VISA),
                Arguments.of("4024007129709", CardBrand.VISA),
                Arguments.of("5555555555554444", CardBrand.MASTERCARD),
                Arguments.of("5454545454545454", CardBrand.MASTERCARD),
                Arguments.of("5105105105105100", CardBrand.MASTERCARD),
                Arguments.of("5115539318330360", CardBrand.MASTERCARD),
                Arguments.of("5314186182048923", CardBrand.MASTERCARD),
                Arguments.of("5478248873641226", CardBrand.MASTERCARD),
                Arguments.of("5263948154727680", CardBrand.MASTERCARD),
                Arguments.of("5535761752128983", CardBrand.MASTERCARD),
                Arguments.of("378282246310005", CardBrand.AMEX),
                Arguments.of("371449635398431", CardBrand.AMEX),
                Arguments.of("378734493671000", CardBrand.AMEX),
                Arguments.of("379623151957810", CardBrand.AMEX),
                Arguments.of("346813438816208", CardBrand.AMEX),
                Arguments.of("341558756862475", CardBrand.AMEX),
                Arguments.of("372261702648583", CardBrand.AMEX),
                Arguments.of("346011941875148", CardBrand.AMEX),
                Arguments.of("3530111333300000", CardBrand.JCB),
                Arguments.of("3566002020360505", CardBrand.JCB),
                Arguments.of("3539865630059491", CardBrand.JCB),
                Arguments.of("3548938244607770", CardBrand.JCB),
                Arguments.of("3548004503181732", CardBrand.JCB),
                Arguments.of("3578272254540348", CardBrand.JCB),
                Arguments.of("30430900807432", CardBrand.DINERS),
                Arguments.of("30386483001944", CardBrand.DINERS),
                Arguments.of("3868366626607238", CardBrand.DINERS),
                Arguments.of("3929002942603190", CardBrand.DINERS),
                Arguments.of("3860943510777851", CardBrand.DINERS),
                Arguments.of("3945436556982021", CardBrand.DINERS),
                Arguments.of("6508140845532181", CardBrand.DISCOVER),
                Arguments.of("6449228991129506", CardBrand.DISCOVER),
                Arguments.of("6221261625600166", CardBrand.DISCOVER),
                Arguments.of("6440221143033246", CardBrand.DISCOVER),
                Arguments.of("6221260781853411", CardBrand.DISCOVER),
                Arguments.of("6229250867070388", CardBrand.DISCOVER),
                Arguments.of("6250946000000016", CardBrand.CUP),
                Arguments.of("6212190968486472", CardBrand.CUP),
                Arguments.of("6236986899169133", CardBrand.CUP),
                Arguments.of("6255967158026517", CardBrand.CUP),
                Arguments.of("6299435412211372", CardBrand.CUP),
                Arguments.of("6250206274332466", CardBrand.CUP),
                Arguments.of("6380109627429000", CardBrand.INSTA_PAYMENT),
                Arguments.of("6375182292776663", CardBrand.INSTA_PAYMENT),
                Arguments.of("6381141989661820", CardBrand.INSTA_PAYMENT),
                Arguments.of("630495060000000000", CardBrand.LASER),
                Arguments.of("630490017740292441", CardBrand.LASER),
                Arguments.of("5019555544445555", CardBrand.DANKORT),
                Arguments.of("5018272349839732", CardBrand.MAESTRO),
                Arguments.of("5018743255725818", CardBrand.MAESTRO),
                Arguments.of("5020330598442438", CardBrand.MAESTRO),
                Arguments.of("5038475199480178", CardBrand.MAESTRO),
                Arguments.of("5038214620929721", CardBrand.MAESTRO),
                Arguments.of("5038145356703932", CardBrand.MAESTRO),
                Arguments.of("6334580500000000", CardBrand.SOLO),
                Arguments.of("6334900000000005", CardBrand.SOLO),
                Arguments.of("633473060000000000", CardBrand.SOLO),
                Arguments.of("6767622222222222222", CardBrand.SOLO),
                Arguments.of("6767676767676767671", CardBrand.SOLO),
                Arguments.of("199941615319031", CardBrand.UATP),
                Arguments.of("152439665280042", CardBrand.UATP),
                Arguments.of("147837779394708", CardBrand.UATP),
                Arguments.of("174737166732013", CardBrand.UATP),
                Arguments.of("106243171850240", CardBrand.UATP),
                Arguments.of("105980617784514", CardBrand.UATP)
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
                Arguments.of("44and_nope", CardBrand.VISA),
                Arguments.of("nope", CardBrand.UNKNOWN),
                Arguments.of("", CardBrand.UNKNOWN)
        );
    }
}