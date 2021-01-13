/*
 * TransactionMapper.java
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

import com.straal.sdk.data.RedirectUrls;
import com.straal.sdk.data.Transaction;

import java.util.HashMap;
import java.util.Map;

public class TransactionMapper {

    public static Map<String, Object> mapSecure(Transaction transaction, RedirectUrls redirectUrls) {
        Map<String, Object> result = TransactionMapper.map(transaction);
        result.put("authentication_3ds", mapRedirectUrls(redirectUrls));
        return result;
    }

    @Deprecated
    public static Map<String, Object> map3DSecure(Transaction transaction, RedirectUrls redirectUrls) {
        Map<String, Object> result = TransactionMapper.map(transaction);
        result.put("success_url", redirectUrls.successUrl);
        result.put("failure_url", redirectUrls.failureUrl);
        return result;
    }

    private static Map<String, Object> map(Transaction transaction) {
        Map<String, Object> result = new HashMap<>();
        result.put("amount", transaction.amount);
        result.put("currency", transaction.currencyCode.name().toLowerCase());
        if (transaction.reference != null) result.put("reference", transaction.reference);
        return result;
    }

    private static Map<String, Object> mapRedirectUrls(RedirectUrls redirectUrls) {
        Map<String, Object> result = new HashMap<>();
        result.put("success_url", redirectUrls.successUrl);
        result.put("failure_url", redirectUrls.failureUrl);
        return result;
    }
}
