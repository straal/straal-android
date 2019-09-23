/*
 * HttpCallableTest.java
 * Created by Arkadiusz Różalski on 26.01.18
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

import com.straal.sdk.exceptions.UnauthorizedException;
import com.straal.sdk.http.HttpClient;
import com.straal.sdk.http.HttpException;
import com.straal.sdk.http.HttpRequestException;
import com.straal.sdk.http.HttpResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("HttpCallable")
class HttpCallableTest {
    private HttpCallable httpCallable;
    private HttpClient httpClient = mock(HttpClient.class);
    private MapToJsonCallable mapToJsonCallable = mock(MapToJsonCallable.class);

    @DisplayName("should return http response")
    @Test
    void returnResponse() throws Exception {
        HttpResponse response = new HttpResponse(200, "success", Collections.emptyMap());
        when(httpClient.post(any(), any())).thenReturn(response);
        httpCallable = createHttpCallable();
        HttpResponse outputResponse = httpCallable.call();
        assertEquals(response.code, outputResponse.code);
        assertEquals(response.body, outputResponse.body);
    }

    @DisplayName("should throw http request exception")
    @Test
    void throwException() throws HttpRequestException {
        when(httpClient.post(any(), any())).thenThrow(HttpRequestException.class);
        httpCallable = createHttpCallable();
        assertThrows(HttpRequestException.class, httpCallable::call);
    }

    @DisplayName("user is not authorized")
    @Test
    void unauthorizedException() throws Exception {
        int code = 401;
        String message = "sample unauthorized message";
        when(httpClient.post(any(), any())).thenReturn(new HttpResponse(code, message, Collections.emptyMap()));
        httpCallable = createHttpCallable();
        HttpException exception = assertThrows(UnauthorizedException.class, httpCallable::call);
        assertEquals(code, exception.code);
        assertTrue(exception.message.contains(message));
    }

    @DisplayName("http response is not successful")
    @Test
    void httpException() throws Exception {
        int code = 402;
        String message = "sample error message";
        when(httpClient.post(any(), any())).thenReturn(new HttpResponse(code, message, Collections.emptyMap()));
        httpCallable = createHttpCallable();
        HttpException exception = assertThrows(HttpException.class, httpCallable::call);
        assertEquals(code, exception.code);
        assertTrue(exception.message.contains(message));
    }

    private HttpCallable createHttpCallable() {
        return new HttpCallable(mapToJsonCallable, httpClient, "sample endpoint");
    }
}
