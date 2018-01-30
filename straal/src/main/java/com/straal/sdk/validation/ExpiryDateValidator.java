/*
 * ExpiryDateValidator.java
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

import com.straal.sdk.card.CreditCard;

import java.util.Calendar;
import java.util.EnumSet;

class ExpiryDateValidator implements CardValidator {
    private final Calendar calendar;

    ExpiryDateValidator(Calendar calendar) {
        this.calendar = calendar;
    }

    ExpiryDateValidator() {
        this(Calendar.getInstance());
    }

    @Override
    public EnumSet<ValidationResult> validate(CreditCard creditCard) {
        EnumSet<ValidationResult> errors = ValidationResult.emptySet();
        if (!isMonthValid(creditCard)) errors.add(ValidationResult.EXPIRY_DATE_INVALID);
        if (isCardExpired(creditCard)) errors.add(ValidationResult.CARD_EXPIRED);
        return (errors.isEmpty()) ? EnumSet.of(ValidationResult.VALID) : errors;
    }

    private boolean isMonthValid(CreditCard creditCard) {
        return !(creditCard.expiryDate.month < 1 || creditCard.expiryDate.month > 12);
    }

    private boolean isCardExpired(CreditCard creditCard) {
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        return creditCard.expiryDate.year < currentYear || creditCard.expiryDate.year == currentYear && creditCard.expiryDate.month < currentMonth;
    }
}
