/*
 * Transaction.java
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

package com.straal.sdk.data;

import androidx.annotation.Nullable;

/**
 * Data class for details of transaction to be performed.
 */
public class Transaction {
    public final int amount;
    public final CurrencyCode currencyCode;
    @Nullable
    public final String reference;

    /**
     * @param amount    amount of money in currency's indivisible unit (e.g. cents for USD)
     * @param currencyCode code of currency used in transaction
     * @param reference a unique String to identify this transaction on your backend service. This parameter is optional
     */
    public Transaction(int amount, CurrencyCode currencyCode, @Nullable String reference) {
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.reference = reference;
    }
}
