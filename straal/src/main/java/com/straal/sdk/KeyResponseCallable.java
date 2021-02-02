/*
 * KeyResponseCallable.java
 * Created by Arkadiusz Różalski on 26.01.18
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

import com.straal.sdk.exceptions.ResponseParseException;
import com.straal.sdk.response.KeyResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Callable;

class KeyResponseCallable implements Callable<KeyResponse> {
    private final Callable<JSONObject> jsonCallable;

    KeyResponseCallable(Callable<JSONObject> jsonResponseCallable) {
        this.jsonCallable = jsonResponseCallable;
    }

    @Override
    public KeyResponse call() throws Exception {
        JSONObject jsonObject = jsonCallable.call();
        try {
            return new KeyResponse(
                    jsonObject.getString("id"),
                    jsonObject.getString("permission"),
                    jsonObject.getString("key"),
                    jsonObject.getInt("ttl"),
                    jsonObject.getLong("created_at")
            );
        } catch (JSONException e) {
            throw new ResponseParseException("Crypt key response didn't contain expected data", e);
        }
    }
}
