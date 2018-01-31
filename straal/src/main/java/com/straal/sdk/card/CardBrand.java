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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Enum representation of credit card types supported by this SDK.
 */
public enum CardBrand {
    VISA_ELECTRON("Visa Electron", "^(4026|417500|4405|4508|4844|491(3|7))"),
    SWITCH("Switch", "^(4903|4905|4911|4936|564182|633110|6333|6759)", Arrays.asList(16, 18, 19)),
    VISA("Visa", "^4", Arrays.asList(13, 16)),
    MASTERCARD("MasterCard", "^5[1-5]"),
    AMEX("American Express", "^3[47]", Collections.singletonList(15), 4),
    JBC("JBC", "^35(28|29|[3-8][0-9])"),
    BANKCARD("Bankcard", "^5610|56022[1-5]"),
    DINERS("Diners", "^(30[0-5]|309|36|38|39|54|55)", Arrays.asList(14, 16)),
    DISCOVER("Discover", "^(65|64[4-9]|6011|622126|622925)"),
    CUP("China UnionPay", "^(62|603367)", Arrays.asList(16, 17, 18, 19)),
    INTER_PAYMENT("Inter Payment", "^636", Arrays.asList(16, 17, 18, 19)),
    INSTA_PAYMENT("Insta Payment", "^63[7-9]"),
    LASER("Laser", "^(6304|6706|6771|6709)", Arrays.asList(16, 17, 18, 19)),
    DANKORT("Dankort", "^5019"),
    MAESTRO("Maestro", "^(50|5[6-9]|6019|603220)", Arrays.asList(12, 13, 14, 15, 16, 17, 18, 19)),
    SOLO("Solo", "^(6334|6767)", Arrays.asList(16, 18, 19)),
    UATP("Universal Air Travel Plan", "^1", Collections.singletonList(15)),
    UNKNOWN("Unknown", "", Collections.singletonList(0), 0);

    public final String name;
    public final String identifyPattern;
    public final SortedSet<Integer> numberLengths;
    public final int cvvLength;

    CardBrand(String name, String identifyPattern) {
        this(name, identifyPattern, Collections.singletonList(16), 3);
    }

    CardBrand(String name, String identifyPattern, List<Integer> numberLengths) {
        this(name, identifyPattern, numberLengths, 3);
    }

    CardBrand(String name, String identifyPattern, List<Integer> numberLengths, int cvvLength) {
        this.name = name;
        this.identifyPattern = identifyPattern + ".*";
        this.numberLengths = new TreeSet<>(numberLengths);
        this.cvvLength = cvvLength;
    }
}
