/*
 * CardholderName.java
 * Created by Konrad Kowalewski on 26.01.18
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
 * Wrapper class for cardholder's full name.
 * Remember that both first name and last name must be at least 2 characters long.
 */
public class CardholderName {
    public final String value;

    /**
     * @param firstName cardholder's first name
     * @param lastName  cardholder's last name
     */
    public CardholderName(String firstName, String lastName) {
        this(StringUtils.unwrapNull(firstName) + " " + StringUtils.unwrapNull(lastName));
    }

    /**
     * @param fullName cardholder's first name and last name separated by space (as written on the card).
     */
    public CardholderName(String fullName) {
        this.value = StringUtils.unwrapNull(fullName);
    }

    /**
     * @return sanitized cardholder's full name
     */
    public String getFullName() {
        return sanitize(value);
    }

    private static String sanitize(String fullName) {
        return StringUtils.unwrapNull(fullName).replaceAll("\\s+", " ").trim();
    }
}
