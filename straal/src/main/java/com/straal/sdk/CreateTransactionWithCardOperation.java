/*
 * CreateTransactionWithCardOperation.java
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

package com.straal.sdk;

import com.straal.sdk.card.CreditCard;
import com.straal.sdk.data.RedirectUrls;
import com.straal.sdk.data.Transaction;
import com.straal.sdk.response.StraalEncrypted3ds2Response;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * A Straal encrypted operation, which will perform a transaction using a credit card that will be registered in Straal.
 *
 * @see StraalOperation
 * @see <a href="https://api-reference.straal.com/#resources-transactions-create-a-transaction-with-a-card-using-cryptkey">'Create transaction' with card in Straal API docs</a>
 */
public class CreateTransactionWithCardOperation extends StraalEncryptedBaseOperation<StraalEncrypted3ds2Response> {
    public final Transaction transaction;
    public final CreditCard card;
    public final RedirectUrls redirectUrls;
    public final Straal3DSecure2Params params;

    /**
     * @param transaction  transaction you want to perform
     * @param creditCard   data (usually typed by the user) of the credit card to be created
     * @param redirectUrls after the 3D-Secure verification is finished, user will be redirected back to one of the speficied URLs (successUrl or failureUrl) depending on the outcome of the 3D-Secure verification
     * @param params       additional device data required to execute 3D-Secure transaction
     */
    public CreateTransactionWithCardOperation(Transaction transaction, CreditCard creditCard, RedirectUrls redirectUrls, Straal3DSecure2Params params) {
        super(StraalPermissions.CREATE_TRANSACTION_WITH_CARD);
        this.transaction = transaction;
        this.card = creditCard;
        this.redirectUrls = redirectUrls;
        this.params = params;
    }

    @Override
    protected Map<String, Object> getStraalRequestPayload() {
        return DataMapper.map3DSecure2CreditCard(card, params);
    }

    @Override
    protected Map<String, Object> getCryptKeyPayload() {
        Map<String, Object> map = new HashMap<>();
        map.put("permission", permission);
        map.put("transaction", DataMapper.map3DSecure2Transaction(transaction, redirectUrls));
        return map;
    }

    @Override
    protected Callable<StraalEncrypted3ds2Response> getStraalResponseCallable(HttpCallable httpCallable) {
        return new Straal3ds2ResponseCallable(httpCallable, redirectUrls);
    }
}
