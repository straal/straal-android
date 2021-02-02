/*
 * Straal3dsResponseCallable.java
 * Created by Arkadiusz Różalski on 03.09.19
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
 */

package com.straal.sdk;

import com.straal.sdk.data.RedirectUrls;
import com.straal.sdk.exceptions.ResponseParseException;
import com.straal.sdk.http.HttpResponse;
import com.straal.sdk.response.StraalEncrypted3dsResponse;
import com.straal.sdk.response.TransactionStatus;

import java.util.List;
import java.util.concurrent.Callable;

class Straal3dsResponseCallable implements Callable<StraalEncrypted3dsResponse> {
    private final Callable<HttpResponse> responseCallable;
    private final RedirectUrls redirectUrls;

    Straal3dsResponseCallable(Callable<HttpResponse> responseCallable, RedirectUrls redirectUrls) {
        this.responseCallable = responseCallable;
        this.redirectUrls = redirectUrls;
    }

    @Override
    public StraalEncrypted3dsResponse call() throws Exception {
        HttpResponse response = responseCallable.call();
        try {
            String locationUrl = getLocation(response);
            TransactionStatus status = (locationUrl == null) ? TransactionStatus.SUCCESS : TransactionStatus.CHALLENGE_3DS;
            return new StraalEncrypted3dsResponse(getRequestId(response), redirectUrls, unwrapLocation(locationUrl), status);
        } catch (Exception e) {
            throw new ResponseParseException("Response from Straal API didn't contain expected data", e);
        }
    }

    private String getLocation(HttpResponse response) {
        List<String> locations = response.headerFields.get("Location");
        if (locations != null) {
            String locationUrl = locations.get(0);
            return (locationUrl.equals(redirectUrls.successUrl)) ? null : locationUrl;
        }
        return null;
    }

    private String getRequestId(HttpResponse response) throws Exception {
        return new JsonResponseCallable(response).call().getString("request_id");
    }

    private String unwrapLocation(String location) {
        return (location == null) ? "" : location;
    }
}
