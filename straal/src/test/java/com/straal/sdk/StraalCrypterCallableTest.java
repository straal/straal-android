/*
 * StraalCrypterCallableTest.java
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

import com.straal.sdk.response.KeyResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("StraalCrypterCallable")
class StraalCrypterCallableTest {

    @DisplayName("should return crypter")
    @Test
    void createCrypter() throws Exception {
        KeyResponse response = new KeyResponse("0c18967928000123dc8f501fac235c60507b7217d143d11e22b2e59668f0e1872b7830d91531be885d4ff45bbb5e3ecae3389b36b9c93c128de34f81e797a71bdc933262d09a421e");
        KeyResponseCallable keyResponseCallable = mock(KeyResponseCallable.class);
        when(keyResponseCallable.call()).thenReturn(response);
        assertTrue(new StraalCrypterCallable(keyResponseCallable).call() != null);
    }
}
