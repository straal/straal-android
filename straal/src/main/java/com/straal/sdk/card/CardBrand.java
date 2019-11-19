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
    SWITCH("Switch", "^(4903|4905|4911|4936|564182|633110|6333|6759)", Arrays.asList(
            Arrays.asList(4, 4, 4, 4),
            Arrays.asList(4, 4, 4, 6),
            Arrays.asList(4, 4, 4, 4, 3)
    )),
    VISA("Visa", "^4", Arrays.asList(
            Arrays.asList(4, 4, 4, 1),
            Arrays.asList(4, 4, 4, 4),
            Arrays.asList(4, 4, 4, 4, 3)
    )),
    MASTERCARD("MasterCard", "^(5[1-5]|2(22[1-9]|2[3-9][0-9]|[3-6]|7[0-1]|720))"),
    AMEX("American Express", "^3[47]", Collections.singletonList(
            Arrays.asList(4, 6, 5)
    ), 4, true),
    JCB("JCB", "^35(28|29|[3-8][0-9])", Arrays.asList(
            Arrays.asList(4, 4, 4, 4),
            Arrays.asList(4, 4, 4, 5),
            Arrays.asList(4, 4, 4, 6),
            Arrays.asList(4, 4, 4, 4, 3)
    )),
    BANKCARD("Bankcard", "^5610|56022[1-5]"),
    DINERS("Diners", "^(30[0-5]|309|36|38|39|54|55)", Arrays.asList(
            Arrays.asList(4, 6, 4),
            Arrays.asList(4, 4, 4, 3),
            Arrays.asList(4, 4, 4, 4),
            Arrays.asList(4, 4, 4, 5),
            Arrays.asList(4, 4, 4, 6),
            Arrays.asList(4, 4, 4, 4, 3)
    )),
    DISCOVER("Discover", "^(65|64[4-9]|6011|622126|622925)", Arrays.asList(
            Arrays.asList(4, 4, 4, 4),
            Arrays.asList(4, 4, 4, 5),
            Arrays.asList(4, 4, 4, 6),
            Arrays.asList(4, 4, 4, 4, 3)
    )),
    CUP("China UnionPay", "^(62|603367)", Arrays.asList(
            Arrays.asList(4, 4, 4, 4),
            Arrays.asList(4, 4, 4, 5),
            Arrays.asList(4, 4, 4, 6),
            Arrays.asList(4, 4, 4, 4, 3)
    ), 3, false),
    INTER_PAYMENT("Inter Payment", "^636", Arrays.asList(
            Arrays.asList(4, 4, 4, 4),
            Arrays.asList(4, 4, 4, 5),
            Arrays.asList(4, 4, 4, 6),
            Arrays.asList(4, 4, 4, 4, 3)
    )),
    INSTA_PAYMENT("Insta Payment", "^63[7-9]"),
    LASER("Laser", "^(6304|6706|6771|6709)", Arrays.asList(
            Arrays.asList(4, 4, 4, 4),
            Arrays.asList(4, 4, 4, 5),
            Arrays.asList(4, 4, 4, 6),
            Arrays.asList(4, 4, 4, 4, 3)
    )),
    DANKORT("Dankort", "^5019"),
    MAESTRO("Maestro", "^(50|5[6-9]|6019|603220)", Arrays.asList(
            Arrays.asList(4, 4, 4),
            Arrays.asList(4, 4, 5),
            Arrays.asList(4, 4, 6),
            Arrays.asList(4, 4, 4, 3),
            Arrays.asList(4, 4, 4, 4),
            Arrays.asList(4, 4, 4, 5),
            Arrays.asList(4, 4, 4, 6),
            Arrays.asList(4, 4, 4, 4, 3)
    )),
    SOLO("Solo", "^(6334|6767)", Arrays.asList(
            Arrays.asList(4, 4, 4, 4),
            Arrays.asList(4, 4, 4, 6),
            Arrays.asList(4, 4, 4, 4, 3)
    )),
    UATP("Universal Air Travel Plan", "^1", Collections.singletonList(
            Arrays.asList(4, 5, 6)
    )),
    UNKNOWN("Unknown", "", Collections.singletonList(Collections.singletonList(0)), 0, true);

    public final String brand;
    public final String identifyPattern;
    public final List<List<Integer>> groupings;
    public final int cvvLength;
    public final boolean requiresLuhn;

    CardBrand(String brand, String identifyPattern) {
        this(brand, identifyPattern, Collections.singletonList(Arrays.asList(4, 4, 4, 4)), 3, true);
    }

    CardBrand(String brand, String identifyPattern, List<List<Integer>> groupings) {
        this(brand, identifyPattern, groupings, 3, true);
    }

    CardBrand(String brand, String identifyPattern, List<List<Integer>> groupings, int cvvLength, boolean requiresLuhn) {
        this.brand = brand;
        this.identifyPattern = identifyPattern + ".*";
        this.groupings = groupings;
        this.cvvLength = cvvLength;
        this.requiresLuhn = requiresLuhn;
    }

    public SortedSet<Integer> getNumberLengths() {
        SortedSet<Integer> lengths = new TreeSet<>();
        for (List<Integer> grouping: groupings) lengths.add(sumGrouping(grouping));
        return lengths;
    }

    private int sumGrouping(List<Integer> grouping) {
        int result = 0;
        for (int item: grouping) result += item;
        return result;
    }
}
