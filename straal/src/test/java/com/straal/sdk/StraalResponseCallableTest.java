/*
 * StraalResponseCallableTest.java
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
import com.straal.sdk.http.HttpResponse;
import com.straal.sdk.response.StraalEncryptedResponse;

import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("StraalResponseCallable")
class StraalResponseCallableTest {
    private StraalResponseCallable straalResponseCallable;
    private HttpCallable httpResponseCallable = mock(HttpCallable.class);

    @DisplayName("should return valid response")
    @Test
    void returnResponse() throws Exception {
        int code = 200;
        String requestId = "sample request id";
        HttpResponse response = new HttpResponse(code, "{\"request_id\":" + requestId + "}", Collections.emptyMap());
        when(httpResponseCallable.call()).thenReturn(response);
        straalResponseCallable = createStraalResponseCallable();
        StraalEncryptedResponse straalResponse = straalResponseCallable.call();
        assertEquals(requestId, straalResponse.requestId);
    }

    @DisplayName("should throw json exception on invalid json")
    @Test
    void throwParseException() throws Exception {
        when(httpResponseCallable.call()).thenReturn(new HttpResponse(200, "invalid body", Collections.emptyMap()));
        straalResponseCallable = createStraalResponseCallable();
        assertThrows(JSONException.class, straalResponseCallable::call);
    }

    @DisplayName("should throw parse exception when json has wrong data")
    @Test
    void invalidDataInJson() throws Exception {
        when(httpResponseCallable.call()).thenReturn(new HttpResponse(200, "{\"a\":\"A\"}", Collections.emptyMap()));
        straalResponseCallable = createStraalResponseCallable();
        assertThrows(ResponseParseException.class, straalResponseCallable::call);
    }

    private StraalResponseCallable createStraalResponseCallable() {
        return new StraalResponseCallable(new JsonResponseCallable(httpResponseCallable));
    }
}
