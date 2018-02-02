/*
 * ValidationResult.java
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

import java.util.EnumSet;

/**
 * Possible validation results that can be returned from card validators.
 *
 * @see CardValidator
 */
public enum ValidationResult {
    /**
     * Card is valid.
     * Regardless of what other ValidationResult is present, this card can be used to make payments.
     */
    VALID,
    /**
     * Card number doesn't match any known card brand.
     *
     * @see com.straal.sdk.card.CardBrand
     */
    CARD_PATTERN_NOT_MATCHED,
    /**
     * Card number contains a non-numeric character.
     */
    CARD_NUMBER_NOT_NUMERIC,
    /**
     * Card number may or must be longer, depending on what other results are returned.
     * If VALID is also returned, there could be another valid number (for a given card brand) which is longer than the one being validated.
     */
    CARD_NUMBER_INCOMPLETE,
    /**
     * Card number is too long for given card brand.
     */
    CARD_NUMBER_TOO_LONG,
    /**
     * Card number doesn't pass the Luhn check.
     */
    LUHN_TEST_FAILED,
    /**
     * Cardholder name must have at least 2 characters in both first and last name.
     */
    CARDHOLDER_NAME_INVALID,
    /**
     * CVV/CVC code is invalid (contains non-numeric characters).
     */
    CVV_INVALID,
    /**
     * CVV/CVC code is too short for given card brand.
     */
    CVV_INCOMPLETE,
    /**
     * Expiry date doesn't represent a valid date.
     */
    EXPIRY_DATE_INVALID,
    /**
     * Card already expired and cannot be used.
     */
    CARD_EXPIRED;

    static EnumSet<ValidationResult> emptySet() {
        return EnumSet.noneOf(ValidationResult.class);
    }
}
