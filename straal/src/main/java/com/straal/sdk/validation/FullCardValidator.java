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

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class FullCardValidator implements CardValidator {
    @Override
    public EnumSet<ValidationResult> validate(CreditCard creditCard) {
        CardBrand cardBrand = creditCard.getBrand();
        if (cardBrand == CardBrand.UNKNOWN) return EnumSet.of(ValidationResult.CARD_PATTERN_NOT_MATCHED);
        EnumSet<ValidationResult> finalResult = ValidationResult.emptySet();
        List<EnumSet<ValidationResult>> criteriaList = validateAllCriteria(creditCard);
        combineAllCriteriaIntoFinal(finalResult, criteriaList);
        removeValidIfAnyCriteriaInvalid(finalResult, criteriaList);
        return finalResult;
    }

    private List<EnumSet<ValidationResult>> validateAllCriteria(CreditCard creditCard) {
        EnumSet<ValidationResult> nameResults = new CardholderNameValidator().validate(creditCard);
        EnumSet<ValidationResult> numberResults = new CardNumberValidator().validate(creditCard);
        EnumSet<ValidationResult> expiryResults = new ExpiryDateValidator().validate(creditCard);
        EnumSet<ValidationResult> cvvResults = new CvvValidator().validate(creditCard);
        return Arrays.asList(nameResults, numberResults, expiryResults, cvvResults);
    }

    private void combineAllCriteriaIntoFinal(EnumSet<ValidationResult> finalResult, List<EnumSet<ValidationResult>> resultsList) {
        for (EnumSet<ValidationResult> resultEnumSet : resultsList) {
            finalResult.addAll(resultEnumSet);
        }
    }

    private void removeValidIfAnyCriteriaInvalid(EnumSet<ValidationResult> finalResult, List<EnumSet<ValidationResult>> resultsList) {
        for (EnumSet<ValidationResult> resultEnumSet : resultsList) {
            if (!resultEnumSet.contains(ValidationResult.VALID)) finalResult.remove(ValidationResult.VALID);
        }
    }
}
