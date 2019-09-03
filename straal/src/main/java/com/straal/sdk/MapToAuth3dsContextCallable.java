/*
 * MapToAuth3dsContextCallable.java
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

import com.straal.sdk.data.RedirectUrls;
import com.straal.sdk.http.HttpResponse;
import com.straal.sdk.response.Auth3dsContext;

import java.util.concurrent.Callable;

class MapToAuth3dsContextCallable implements Callable<Auth3dsContext> {
    private final Callable<HttpResponse> responseCallable;
    private final RedirectUrls redirectUrls;

    MapToAuth3dsContextCallable(Callable<HttpResponse> responseCallable, RedirectUrls redirectUrls) {
        this.responseCallable = responseCallable;
        this.redirectUrls = redirectUrls;
    }

    MapToAuth3dsContextCallable(HttpResponse httpResponse, RedirectUrls redirectUrls) {
        this.responseCallable = SimpleSourceCallable.of(httpResponse);
        this.redirectUrls = redirectUrls;
    }

    @Override
    public Auth3dsContext call() throws Exception {
        String locationUrl = responseCallable.call().headerFields.get("Location").get(0);
        return new Auth3dsContext(locationUrl, redirectUrls);
    }
}
