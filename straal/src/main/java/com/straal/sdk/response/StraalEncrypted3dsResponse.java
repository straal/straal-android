/*
 * StraalEncrypted3dsResponse.java
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

package com.straal.sdk.response;

import com.straal.sdk.data.RedirectUrls;

/**
 * @see com.straal.sdk.response.StraalEncryptedResponse
 * This response is returned from init 3ds operation.
 */
public class StraalEncrypted3dsResponse extends StraalEncryptedResponse {
    /**
     * User will be redirected back to one of the speficied URLs (successUrl or failureUrl) depending on the outcome of the 3D-Secure verification.
     */
    public final RedirectUrls redirectUrls;
    /**
     * Redirect url used to complete the 3D-Secure verification process
     */
    public final String locationUrl;
    /**
     * Transaction status determining whenever further action is needed to complete transaction
     */
    public final TransactionStatus status;

    public StraalEncrypted3dsResponse(String requestId, RedirectUrls redirectUrls, String locationUrl, TransactionStatus status) {
        super(requestId);
        this.redirectUrls = redirectUrls;
        this.locationUrl = locationUrl;
        this.status = status;
    }
}
