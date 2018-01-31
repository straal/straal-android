/*
 * FullCardValidator.java
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

public class FullCardValidator implements CardValidator {
    @Override
    public EnumSet<ValidationResult> validate(CreditCard creditCard) {
        CardBrand cardBrand = creditCard.getBrand();
        if (cardBrand == CardBrand.UNKNOWN) return EnumSet.of(ValidationResult.CARD_PATTERN_NOT_MATCHED);
        EnumSet<ValidationResult> results = new CardholderNameValidator().validate(creditCard);
        EnumSet<ValidationResult> numberResults = new CardNumberValidator().validate(creditCard);
        results.addAll(numberResults);
        results.addAll(new ExpiryDateValidator().validate(creditCard));
        results.addAll(new CvvValidator().validate(creditCard));
        if (isFullResultInvalid(results, numberResults)) {
            results.remove(ValidationResult.VALID);
        }
        return results;
    }

    private boolean isFullResultInvalid(EnumSet<ValidationResult> fullResults, EnumSet<ValidationResult> numberResults) {
        return (!(fullResults.equals(EnumSet.of(ValidationResult.INCOMPLETE, ValidationResult.VALID))
                || fullResults.equals(EnumSet.of(ValidationResult.VALID)))
                || numberResults.equals(EnumSet.of(ValidationResult.INCOMPLETE)));
    }
}
