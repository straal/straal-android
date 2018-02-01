/*
 * CardNumberValidator.java
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

package com.straal.sdk.validation;

import com.straal.sdk.card.CardBrand;
import com.straal.sdk.card.CreditCard;

import java.util.EnumSet;
import java.util.SortedSet;

class CardNumberValidator implements CardValidator {
    @Override
    public EnumSet<ValidationResult> validate(CreditCard creditCard) {
        EnumSet<ValidationResult> results = ValidationResult.emptySet();
        String sanitizedNumber = sanitize(creditCard.number);
        CardBrand brand = creditCard.getBrand();
        int lastLength = brand.numberLengths.last();
        int numberLength = sanitizedNumber.length();
        if (!sanitizedNumber.matches("\\d+")) results.add(ValidationResult.CARD_NUMBER_NOT_NUMERIC);
        if (numberLength < lastLength)
            results.add(ValidationResult.CARD_NUMBER_INCOMPLETE);
        if (numberLength > lastLength)
            results.add(ValidationResult.CARD_NUMBER_TOO_LONG);
        if (!Luhn.validate(sanitizedNumber)) results.add(ValidationResult.LUHN_TEST_FAILED);
        if (isFullResultValid(results) || isIncompleteResultValid(results, brand.numberLengths, numberLength))
            results.add(ValidationResult.VALID);
        return results;
    }

    private boolean isFullResultValid(EnumSet<ValidationResult> results) {
        return results.isEmpty();
    }

    private boolean isIncompleteResultValid(EnumSet<ValidationResult> results, SortedSet<Integer> numberLengths, int numberLength) {
        return (results.equals(EnumSet.of(ValidationResult.CARD_NUMBER_INCOMPLETE)) && numberLengths.contains(numberLength));
    }

    private String sanitize(String name) {
        return name.replace(" ", "").replace("-", "");
    }
}