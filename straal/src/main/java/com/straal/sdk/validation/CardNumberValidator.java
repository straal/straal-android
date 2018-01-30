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

class CardNumberValidator implements CardValidator {
    @Override
    public EnumSet<ValidationError> validate(CreditCard creditCard) {
        EnumSet<ValidationError> errors = ValidationError.emptySet();
        String sanitizedNumber = sanitize(creditCard.number);
        CardBrand brand = creditCard.getBrand();
        if (!sanitizedNumber.matches("\\d+")) errors.add(ValidationError.CARD_NUMBER_NOT_NUMERIC);
        if (brand == CardBrand.UNKNOWN) {
            errors.add(ValidationError.CARD_PATTERN_NOT_MATCHED);
            return errors;
        }
        if (sanitizedNumber.length() < brand.numberLengths.first())
            errors.add(ValidationError.CARD_NUMBER_INCOMPLETE);
        if (sanitizedNumber.length() > brand.numberLengths.last())
            errors.add(ValidationError.CARD_NUMBER_TOO_LONG);
        if (!Luhn.validate(sanitizedNumber)) errors.add(ValidationError.LUHN_TEST_FAILED);
        return errors;
    }

    private String sanitize(String name) {
        return name.replace(" ", "").replace("-", "");
    }
}
