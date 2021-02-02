/*
 * CreateCardOperation.java
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
import com.straal.sdk.response.StraalEncryptedResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * A Straal encrypted operation, which will create a new credit card in Straal.
 *
 * @see StraalOperation
 * @see <a href="https://api-reference.straal.com/#resources-cards-create-a-card-using-cryptkey">'Create card' in Straal API docs</a>
 * @deprecated use {@link CreateTransactionWithCardOperation } instead
 */
@Deprecated
public class CreateCardOperation extends StraalEncryptedBaseOperation<StraalEncryptedResponse> {
    public final CreditCard card;

    /**
     * @param creditCard data (usually typed by the user) of the credit card to be created
     */
    public CreateCardOperation(CreditCard creditCard) {
        super(StraalPermissions.CREATE_CARD_PERMISSION);
        this.card = creditCard;
    }

    @Override
    protected Map<String, Object> getStraalRequestPayload(Straal.Config config) {
        return CardMapper.map(card);
    }

    @Override
    protected Map<String, Object> getCryptKeyPayload() {
        Map<String, Object> map = new HashMap<>();
        map.put("permission", permission);
        return map;
    }

    @Override
    protected Callable<StraalEncryptedResponse> getStraalResponseCallable(HttpCallable httpCallable) {
        return new StraalResponseCallable(new JsonResponseCallable(httpCallable));
    }
}
