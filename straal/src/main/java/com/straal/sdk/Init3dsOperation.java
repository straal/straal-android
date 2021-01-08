/*
 * Init3dsOperation.java
 * Created by Arkadiusz Różalski on 03.09.19
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
import com.straal.sdk.response.StraalEncrypted3dsResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @see StraalOperation
 * @see <a href="https://api-reference.straal.com/#resources-3d-secure-authentications-initialize-3d-secure-process-with-a-cryptkey">'Initialize 3D-Secure' with transaction and card in Straal API docs</a>
 * @deprecated use {@link CreateTransactionWithCardOperation } instead
 * A Straal encrypted operation, which will initialize 3D-Secure process for a transaction using a credit card that will be registered in Straal.
 */
@Deprecated
public class Init3dsOperation extends StraalEncryptedBaseOperation<StraalEncrypted3dsResponse> {
    public final Transaction transaction;
    public final CreditCard card;
    public final RedirectUrls redirectUrls;

    /**
     * @param transaction transaction you want to perform
     * @param creditCard  data (usually typed by the user) of the credit card to be created
     * @param redirectUrls  after the 3D-Secure verification is finished, user will be redirected back to one of the speficied URLs (successUrl or failureUrl) depending on the outcome of the 3D-Secure verification
     */
    public Init3dsOperation(Transaction transaction, CreditCard creditCard, RedirectUrls redirectUrls) {
        super(StraalPermissions.AUTHENTICATION_3DS);
        this.transaction = transaction;
        this.card = creditCard;
        this.redirectUrls = redirectUrls;
    }

    @Override
    protected Map<String, Object> getStraalRequestPayload() {
        return DataMapper.mapCreditCard(card);
    }

    @Override
    protected Map<String, Object> getCryptKeyPayload() {
        Map<String, Object> map = new HashMap<>();
        map.put("permission", permission);
        map.put("transaction", DataMapper.map3DSecureTransaction(transaction, redirectUrls));
        return map;
    }

    @Override
    protected Callable<StraalEncrypted3dsResponse> getStraalResponseCallable(HttpCallable httpCallable) {
        return new Straal3dsResponseCallable(httpCallable, redirectUrls);
    }
}
