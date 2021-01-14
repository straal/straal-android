/*
 * Straal3dsResponseCallableTest.java
 * Created by Kamil Czanik on 14.01.2021
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
import com.straal.sdk.response.StraalEncrypted3dsResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.straal.sdk.response.TransactionStatus.CHALLENGE_3DS;
import static com.straal.sdk.response.TransactionStatus.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("Straal3dsResponseCallable")
class Straal3dsResponseCallableTest {
    private Straal3dsResponseCallable straal3DSResponseCallable;
    private HttpCallable httpResponseCallable = mock(HttpCallable.class);
    private RedirectUrls redirectUrls = new RedirectUrls("url/success", "url/failure");

    @BeforeEach
    void createStraal3DSResponseCallable() {
        straal3DSResponseCallable = new Straal3dsResponseCallable(httpResponseCallable, redirectUrls);
    }

    @DisplayName("Should return response with Transaction.SUCCESS when response is successful and location is nul")
    @Test
    void shouldReturnResponseWithSuccessStatusWhenSuccessfulAndLocationIsNull() throws Exception {
        String requestId = "request-id";
        mockHttpCallableResponse(requestId, Collections.emptyMap());
        StraalEncrypted3dsResponse expected = new StraalEncrypted3dsResponse(requestId, redirectUrls, "", SUCCESS);
        StraalEncrypted3dsResponse response = straal3DSResponseCallable.call();
        assertResponse(expected, response);
    }

    @DisplayName("Should return response with Transaction.SUCCESS when response is successful and location equals successUrl")
    @Test
    void shouldReturnResponseWithSuccessStatusWhenSuccessfulAndLocationEqualsSuccessUrl() throws Exception {
        String requestId = "request-id";
        mockHttpCallableResponse(requestId, locationHeaders(redirectUrls.successUrl));
        StraalEncrypted3dsResponse expected = new StraalEncrypted3dsResponse(requestId, redirectUrls, "", SUCCESS);
        StraalEncrypted3dsResponse response = straal3DSResponseCallable.call();
        assertResponse(expected, response);
    }

    @DisplayName("Should return response with Transaction.CHALLENGE_3DS when response is successful and location is not null")
    @Test
    void shouldReturnResponseWithChallenge3DSWhenSuccessfulAndLocationUrlIsAvailable() throws Exception {
        String requestId = "request-id";
        String locationUrl = "url/location";
        mockHttpCallableResponse(requestId, locationHeaders(locationUrl));
        StraalEncrypted3dsResponse expected = new StraalEncrypted3dsResponse(requestId, redirectUrls, locationUrl, CHALLENGE_3DS);
        StraalEncrypted3dsResponse response = straal3DSResponseCallable.call();
        assertResponse(expected, response);
    }

    @DisplayName("Should throw exception when successful and request_id is null")
    @Test
    void shouldThrowExceptionWhenSuccessfulAndRequestIdIsNull() {
        Executable executable = new Executable() {
            @Override
            public void execute() throws Throwable {
                HttpResponse httpResponse = new HttpResponse(200, "{}", Collections.emptyMap());
                when(httpResponseCallable.call()).thenReturn(httpResponse);
                straal3DSResponseCallable.call();
            }
        };
        assertThrows(ResponseParseException.class, executable);
    }

    private Map<String, List<String>> locationHeaders(String locationUrl) {
        HashMap<String, List<String>> result = new HashMap<>();
        List<String> locations = new ArrayList<>();
        locations.add(locationUrl);
        result.put("Location", locations);
        return result;
    }

    private void mockHttpCallableResponse(String requestId, Map<String, List<String>> headerFields) throws Exception {
        HttpResponse httpResponse = new HttpResponse(200, "{\"request_id\":" + requestId + "}", headerFields);
        when(httpResponseCallable.call()).thenReturn(httpResponse);
    }

    private void assertResponse(StraalEncrypted3dsResponse expected, StraalEncrypted3dsResponse actual) {
        assertEquals(expected.status, actual.status);
        assertEquals(expected.requestId, actual.requestId);
        assertEquals(expected.locationUrl, actual.locationUrl);
        assertEquals(expected.redirectUrls, actual.redirectUrls);
    }
}
