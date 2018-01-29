/*
 * CreditCardTest.java
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("CreditCard")
class CreditCardTest {
    @Nested
    @DisplayName("should have year over 2000 when initialized with")
    class YearBelowHundred {
        @Test
        void zero() {
            CreditCard creditCard = createCreditCardFromInt(0);
            Assertions.assertEquals(2000, creditCard.expiryDate.year);
        }

        @Test
        void eleven() {
            CreditCard creditCard = createCreditCardFromInt(11);
            Assertions.assertEquals(2011, creditCard.expiryDate.year);
        }

        @Test
        void ninetyNine() {
            CreditCard creditCard = createCreditCardFromInt(99);
            Assertions.assertEquals(2099, creditCard.expiryDate.year);
        }

        @Test
        void twoThousandTwo() {
            CreditCard creditCard = createCreditCardFromInt(2002);
            Assertions.assertEquals(2002, creditCard.expiryDate.year);
        }

        @Test
        void zeroAsString() {
            CreditCard creditCard = createCreditCardFromString("0");
            Assertions.assertEquals(2000, creditCard.expiryDate.year);
        }

        @Test
        void elevenAsString() {
            CreditCard creditCard = createCreditCardFromString("11");
            Assertions.assertEquals(2011, creditCard.expiryDate.year);
        }

        @Test
        void ninetyNineAsString() {
            CreditCard creditCard = createCreditCardFromString("99");
            Assertions.assertEquals(2099, creditCard.expiryDate.year);
        }

        @Test
        void twoThousandTwoAsString() {
            CreditCard creditCard = createCreditCardFromInt(2002);
            Assertions.assertEquals(2002, creditCard.expiryDate.year);
        }
    }

    @Nested
    @DisplayName("should have year below 2000 when initialized with")
    class YearOverHundred {
        @Test
        void aHundred() {
            CreditCard creditCard = createCreditCardFromInt(100);
            Assertions.assertEquals(100, creditCard.expiryDate.year);
        }

        @Test
        void aThousand() {
            CreditCard creditCard = createCreditCardFromInt(1000);
            Assertions.assertEquals(1000, creditCard.expiryDate.year);
        }

        @Test
        void aHundredAsString() {
            CreditCard creditCard = createCreditCardFromString("100");
            Assertions.assertEquals(100, creditCard.expiryDate.year);
        }

        @Test
        void aThousandAsString() {
            CreditCard creditCard = createCreditCardFromString("1000");
            Assertions.assertEquals(1000, creditCard.expiryDate.year);
        }
    }

    private CreditCard createCreditCardFromInt(int expiryYear) {
        return new CreditCard("Jar Jar", "4111111111111111", "123", new ExpiryDate(12, expiryYear));
    }

    private CreditCard createCreditCardFromString(String expiryYear) {
        return new CreditCard("Jar Jar", "4111111111111111", "123", new ExpiryDate("12", expiryYear));
    }
}