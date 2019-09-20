/*
 * JsonResponseCallableTest.java
 * Created by Konrad Kowalewski on 26.01.18
 * Straal SDK for Android Tests
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

import com.straal.sdk.http.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("JsonResponseCallable")
class JsonResponseCallableTest {
    private HttpCallable httpCallable = mock(HttpCallable.class);

    @DisplayName("should return JSON object when valid json")
    @Test
    void parseValidJson() throws Exception {
        HttpResponse response = new HttpResponse(200, "{\"request_id\":\"alphanumeric123\"}", Collections.emptyMap());
        when(httpCallable.call()).thenReturn(response);
        JSONObject jsonObject = new JsonResponseCallable(httpCallable).call();
        assertEquals(1, jsonObject.length());
        assertEquals("alphanumeric123", jsonObject.getString("request_id"));
    }

    @DisplayName("should throw exception when malformed JSON")
    @Test
    void throwOnInvalidJson() throws Exception {
        HttpResponse response = new HttpResponse(200, "invalid json", Collections.emptyMap());
        when(httpCallable.call()).thenReturn(response);
        assertThrows(JSONException.class, new JsonResponseCallable(httpCallable)::call);
    }
}