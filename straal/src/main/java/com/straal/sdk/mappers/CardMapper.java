/*
 * CardMapper.java
 * Created by Kamil Czanik on 13.01.2021
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
 *
 */

package com.straal.sdk.mappers;

import com.straal.sdk.DeviceInfo;
import com.straal.sdk.card.CreditCard;

import java.util.HashMap;
import java.util.Map;

public class CardMapper {

    public static Map<String, Object> map(CreditCard cardWithName) {
        Map<String, Object> result = new HashMap<>();
        result.put("name", cardWithName.cardholderName.getFullName());
        result.put("number", cardWithName.number.sanitized());
        result.put("cvv", cardWithName.cvv.value);
        result.put("expiry_month", cardWithName.expiryDate.month);
        result.put("expiry_year", cardWithName.expiryDate.year);
        return result;
    }

    public static Map<String, Object> mapSecure(CreditCard card, DeviceInfo deviceInfo) {
        Map<String, Object> result = map(card);
        result.put("browser", BrowserMapper.map(deviceInfo));
        return result;
    }
}
