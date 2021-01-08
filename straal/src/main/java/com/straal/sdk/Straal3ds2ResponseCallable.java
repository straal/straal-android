/*
 * Straal3ds2ResponseCallable.java
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

package com.straal.sdk;

import com.straal.sdk.data.RedirectUrls;
import com.straal.sdk.exceptions.ResponseParseException;
import com.straal.sdk.http.HttpResponse;
import com.straal.sdk.response.StraalEncrypted3ds2Response;

import java.util.List;
import java.util.concurrent.Callable;

class Straal3ds2ResponseCallable implements Callable<StraalEncrypted3ds2Response> {
    private final Callable<HttpResponse> responseCallable;
    private final RedirectUrls redirectUrls;

    Straal3ds2ResponseCallable(Callable<HttpResponse> responseCallable, RedirectUrls redirectUrls) {
        this.responseCallable = responseCallable;
        this.redirectUrls = redirectUrls;
    }

    @Override
    public StraalEncrypted3ds2Response call() throws Exception {
        HttpResponse response = responseCallable.call();
        try {
            List<String> locations = response.headerFields.get("Location");
            String locationUrl = (locations != null) ? locations.get(0) : "";
            String requestId = new JsonResponseCallable(response).call().getString("request_id");
            return new StraalEncrypted3ds2Response(requestId, redirectUrls, locationUrl);
        } catch (Exception e) {
            throw new ResponseParseException("Response from Straal API didn't contain expected data", e);
        }
    }
}
