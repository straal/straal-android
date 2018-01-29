/*
 * CardBrand.java
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

import java.util.regex.Pattern;

/**
 * Enum representation of credit card types supported by this SDK.
 */
public enum CardBrand {
    VISA("Visa", "^4\\d+", "^(4\\d{15})$"),
    MASTERCARD("MasterCard", "^5[1-5]\\d*", "^5[1-5]\\d{14}$"),
    MAESTRO("Maestro", "^(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d*", "^(5018|5020|5038|5612|5893|6304|6759|6761|6762|6763|0604|6390)\\d{8,15}$"),
    AMEX("American Express", "^3[47]\\d*", "^3[47]\\d{13}$", "####,######,#####", 4),
    UNKNOWN("Unknown", "\\d*", "\\d*", "", 0);

    public final String name;
    public final Pattern identifyPattern;
    public final Pattern fullPattern;
    public final Pattern groupingPattern;
    public final int cvvLength;

    CardBrand(String name, String identifyPattern, String fullPattern) {
        this(name, identifyPattern, fullPattern, "####,####,####,####", 3);
    }

    CardBrand(String name, String identifyPattern, String fullPattern, String groupingPattern, int cvvLength) {
        this.name = name;
        this.identifyPattern = Pattern.compile(identifyPattern);
        this.fullPattern = Pattern.compile(fullPattern);
        this.groupingPattern = Pattern.compile(groupingPattern);
        this.cvvLength = cvvLength;
    }

    public int numberLength() {
        return groupingPattern.pattern().replace(",", "").length();
    }
}
