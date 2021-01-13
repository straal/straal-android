/*
 * DataMapper.java
 * Created by Arkadiusz Różalski on 26.01.18
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

package com.straal.sdk;

import com.straal.sdk.card.CreditCard;
import com.straal.sdk.data.RedirectUrls;
import com.straal.sdk.data.Transaction;

import java.util.HashMap;
import java.util.Map;

class DataMapper {

    static Map<String, Object> map3DSecure2Transaction(Transaction transaction, RedirectUrls redirectUrls) {
        Map<String, Object> result = mapTransaction(transaction);
        result.put("authentication_3ds", mapRedirectUrls(redirectUrls));
        return result;
    }

    static Map<String, Object> mapRedirectUrls(RedirectUrls redirectUrls) {
        Map<String, Object> result = new HashMap<>();
        result.put("success_url", redirectUrls.successUrl);
        result.put("failure_url", redirectUrls.failureUrl);
        return result;
    }

    @Deprecated
    static Map<String, Object> map3DSecureTransaction(Transaction transaction, RedirectUrls redirectUrls) {
        Map<String, Object> result = mapTransaction(transaction);
        result.put("success_url", redirectUrls.successUrl);
        result.put("failure_url", redirectUrls.failureUrl);
        return result;
    }

    static Map<String, Object> mapCreditCard(CreditCard cardWithName) {
        Map<String, Object> result = new HashMap<>();
        result.put("name", cardWithName.cardholderName.getFullName());
        result.put("number", cardWithName.number.sanitized());
        result.put("cvv", cardWithName.cvv.value);
        result.put("expiry_month", cardWithName.expiryDate.month);
        result.put("expiry_year", cardWithName.expiryDate.year);
        return result;
    }

    static Map<String, Object> map3DSecure2CreditCard(CreditCard card, DeviceInfo params) {
        Map<String, Object> result = mapCreditCard(card);
        result.put("browser", map3DSecure2Browser(params));
        return result;
    }

    private static Map<String, Object> map3DSecure2Browser(DeviceInfo params) {
        Map<String, Object> browser = new HashMap<>();
        browser.put("accept_header", "*/*");
        browser.put("language", params.languageTag.value);
        browser.put("user_agent", params.userAgent);
        browser.put("java_enabled", true);
        browser.put("javascript_enabled", true);
        browser.put("timezone", params.timezone.minutesOffset);
        return browser;
    }

    private static Map<String, Object> mapTransaction(Transaction transaction) {
        Map<String, Object> result = new HashMap<>();
        result.put("amount", transaction.amount);
        result.put("currency", transaction.currencyCode.name().toLowerCase());
        if (transaction.reference != null) result.put("reference", transaction.reference);
        return result;
    }
}
