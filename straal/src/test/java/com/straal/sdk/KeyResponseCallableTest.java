/*
 * KeyResponseCallableTest.java
 * Created by Arkadiusz Różalski on 26.01.18
 * Straal SDK for Android Tests
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
import com.straal.sdk.http.HttpRequestException;
import com.straal.sdk.http.HttpResponse;
import com.straal.sdk.response.KeyResponse;

import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("KeyResponseCallable")
class KeyResponseCallableTest {
    private KeyResponseCallable keyResponseCallable;
    private final HttpCallable httpResponseCallable = mock(HttpCallable.class);

    @DisplayName("should return key response given key with metadata")
    @Test
    void returnResponseGivenKeyWithMetadata() throws Exception {
        String id = "sample id";
        String permission = "sample permission";
        String key = "sample key";
        int ttl = 1;
        long createdAt = 1;
        String responseBody = "{\"id\":" + id + ",\"permission\":" + permission + ",\"key\":" + key + ",\"ttl\":" + ttl + ",\"created_at\":" + createdAt + "}";
        when(httpResponseCallable.call()).thenReturn(new HttpResponse(200, responseBody, Collections.emptyMap()));
        keyResponseCallable = createKeyResponseCallable();
        KeyResponse response = keyResponseCallable.call();
        assertEquals(key, response.key);
    }

    @DisplayName("should return key response given only key")
    @Test
    void returnResponseGivenOnlyKey() throws Exception {
        String key = "sample key";
        String responseBody = "{\"key\":" + key + "}";
        when(httpResponseCallable.call()).thenReturn(new HttpResponse(200, responseBody, Collections.emptyMap()));
        keyResponseCallable = createKeyResponseCallable();
        KeyResponse response = keyResponseCallable.call();
        assertEquals(key, response.key);
    }

    @DisplayName("should throw exception when")
    @Nested
    class Exceptions {

        @DisplayName("json is invalid")
        @Test
        void invalidJson() throws Exception {
            when(httpResponseCallable.call()).thenReturn(new HttpResponse(200, "invalid json", Collections.emptyMap()));
            keyResponseCallable = createKeyResponseCallable();
            assertThrows(JSONException.class, keyResponseCallable::call);
        }

        @DisplayName("json has wrong data")
        @Test
        void invalidDataInJson() throws Exception {
            when(httpResponseCallable.call()).thenReturn(new HttpResponse(200, "{\"a\":\"A\"}", Collections.emptyMap()));
            keyResponseCallable = createKeyResponseCallable();
            assertThrows(ResponseParseException.class, keyResponseCallable::call);
        }

        @DisplayName("http client throws exception")
        @Test
        void httpClientException() throws Exception {
            when(httpResponseCallable.call()).thenThrow(HttpRequestException.class);
            keyResponseCallable = createKeyResponseCallable();
            assertThrows(HttpRequestException.class, keyResponseCallable::call);
        }
    }

    private KeyResponseCallable createKeyResponseCallable() {
        return new KeyResponseCallable(new JsonResponseCallable(httpResponseCallable));
    }
}
