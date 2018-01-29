/*
 * HttpCallable.java
 * Created by Arkadiusz Różalski on 26.01.18
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

import com.straal.sdk.exceptions.UnauthorizedException;
import com.straal.sdk.http.HttpClient;
import com.straal.sdk.http.HttpException;
import com.straal.sdk.http.HttpResponse;

import java.util.concurrent.Callable;

class HttpCallable implements Callable<HttpResponse> {
    private final Callable<String> requestBodyCallable;
    private final HttpClient client;
    private final String endpoint;

    HttpCallable(Callable<String> requestBodyCallable, HttpClient client, String endpoint) {
        this.requestBodyCallable = requestBodyCallable;
        this.client = client;
        this.endpoint = endpoint;
    }

    @Override
    public HttpResponse call() throws Exception {
        HttpResponse response = client.post(endpoint, requestBodyCallable.call());
        checkResponse(response);
        return response;
    }

    private void checkResponse(HttpResponse response) throws HttpException {
        if (HttpResponse.isSuccessful(response.code)) return;
        switch (response.code) {
            case 401:
                throw new UnauthorizedException(response.body);
            default:
                throw new HttpException(response.code, response.body);
        }
    }
}
