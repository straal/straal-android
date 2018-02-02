/*
 * CreditCard.java
 * Created by Konrad Kowalewski on 26.01.18
 * Straal SDK for Android
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

public class CreditCard {
    public final CardholderName cardholderName;
    public final CardNumber number;
    public final Cvv cvv;
    public final ExpiryDate expiryDate;

    public CreditCard(String cardholderFullName, String number, String cvv, ExpiryDate expiryDate) {
        this(new CardholderName(cardholderFullName), new CardNumber(number), new Cvv(cvv), expiryDate);
    }

    public CreditCard(CardholderName cardholderName, CardNumber number, Cvv cvv, ExpiryDate expiryDate) {
        this.cardholderName = cardholderName;
        this.number = number;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }

    public CardBrand getBrand() {
        return CardBrandIdentifier.identify(this);
    }
}
