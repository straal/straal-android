/*
 * StraalEncryptedOperation.java
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

import com.straal.sdk.http.HttpClient;
import com.straal.sdk.response.StraalEncryptedResponse;

import java.util.Map;

/**
 * Base class for all <a href="https://api-reference.straal.com/#resources-cryptkeys-create-a-cryptkey">encrypted</a> Straal operations.
 * See {@link StraalOperation} for usage instructions.
 *
 * @see com.straal.sdk.response.StraalEncryptedResponse
 */
public abstract class StraalEncryptedOperation extends StraalOperation<StraalEncryptedResponse> {
    private static final String CRYPT_KEY_ENDPOINT = "/straal/v1/cryptkeys";
    private static final String STRAAL_ENCRYPTED_ENDPOINT = "/encrypted";
    /**
     * Straal permission defining this operation.
     */
    public final String permission;

    StraalEncryptedOperation(String permission) {
        this.permission = permission;
    }

    protected abstract Map<String, Object> getCryptKeyPayload();

    protected abstract Map<String, Object> getStraalRequestPayload();

    @Override
    StraalEncryptedResponse perform(Straal.Config config) throws Exception {
        MapToJsonCallable cryptKeyRequest = new MapToJsonCallable(getCryptKeyPayload());
        KeyResponseCallable cryptKeyResponse = new KeyResponseCallable(new JsonResponseCallable(new HttpCallable(cryptKeyRequest, createMerchantHttpClient(config), CRYPT_KEY_ENDPOINT)));
        StraalCrypterCallable straalCrypterCallable = new StraalCrypterCallable(cryptKeyResponse);
        EncryptCallable encryptCallable = new EncryptCallable(straalCrypterCallable, new MapToJsonCallable(getStraalRequestPayload()));
        StraalResponseCallable straalRequest = new StraalResponseCallable(new JsonResponseCallable(new HttpCallable(encryptCallable, createStraalHttpClient(), STRAAL_ENCRYPTED_ENDPOINT)));
        return straalRequest.call();
    }

    private HttpClient createMerchantHttpClient(Straal.Config config) {
        return HttpClient.createFrom(config.merchantBaseUrl, config.merchantApiHeaders);
    }

    private HttpClient createStraalHttpClient() {
        return HttpClient.createFrom(Straal.Config.API_BASE_URL, Straal.Config.API_HTTP_HEADERS);
    }
}