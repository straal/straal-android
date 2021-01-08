/*
 * StraalEncrypted3ds2Response.java
 * Created by Kamil Czanik on 08.01.2021
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

package com.straal.sdk.response;

import com.straal.sdk.data.RedirectUrls;

/**
 * This response is returned from init 3ds2 operation.
 *
 * @see StraalEncryptedResponse
 */
public class StraalEncrypted3ds2Response extends StraalEncryptedResponse {
    /**
     * User will be redirected back to one of the speficied URLs (successUrl or failureUrl) depending on the outcome of the 3D-Secure verification.
     */
    public final RedirectUrls redirectUrls;
    /**
     * Redirect url used to complete the 3D-Secure verification process
     */
    public final String locationUrl;

    public StraalEncrypted3ds2Response(String requestId, RedirectUrls redirectUrls, String locationUrl) {
        super(requestId);
        this.redirectUrls = redirectUrls;
        this.locationUrl = locationUrl;
    }
}
