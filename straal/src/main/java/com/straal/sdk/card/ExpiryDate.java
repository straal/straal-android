/*
 * ExpiryDate.java
 * Created by Arkadiusz Różalski on 26.01.18
 * Straal SDK for Android
 * Copyright 2021 Straal Sp. z o. o.
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

/**
 * Wrapper class for card's expiry date.
 */
public class ExpiryDate {
    private static final int CENTURY = 2000;
    public final int month;
    public final int year;

    /**
     * @param month integer month representation (1-12)
     * @param year  integer year representation (e.g. 8, 21, 2021)
     */
    public ExpiryDate(int month, int year) {
        this.month = month;
        if (year < 100) {
            this.year = CENTURY + year;
        } else {
            this.year = year;
        }
    }

    /**
     * @param month string month representation (both 0X and X are supported). January is 01 (or 1)
     * @param year string year representation in YY or YYYY format
     */
    public ExpiryDate(String month, String year) {
        this(getNumber(month), getNumber(year));
    }

    private static int getNumber(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
