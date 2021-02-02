/*
 * JsonResponseCallable.java
 * Created by Konrad Kowalewski on 26.01.18
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

import com.straal.sdk.http.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;


class JsonResponseCallable implements Callable<JSONObject> {
    private final Callable<HttpResponse> responseCallable;

    JsonResponseCallable(Callable<HttpResponse> responseCallable) {
        this.responseCallable = responseCallable;
    }

    JsonResponseCallable(HttpResponse httpResponse) {
        this.responseCallable = SimpleSourceCallable.of(httpResponse);
    }

    @Override
    public JSONObject call() throws Exception {
        HttpResponse response = responseCallable.call();
        return parseToJson(response.body);
    }

    private JSONObject parseToJson(String responseBody) throws JSONException {
        return new JSONObject(responseBody);
    }
}
