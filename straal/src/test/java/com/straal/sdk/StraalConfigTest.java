/*
 * StraalConfigTest.java
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

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StraalConfigTest {
    @Test
    void shouldTrimSlashesFromBaseUrl() {
        Straal.Config config = new Straal.Config("/url/url///", Collections.emptyMap(), new DeviceInfo("pl-PL"));
        assertEquals("/url/url", config.merchantBaseUrl);
    }

    @Test
    void shouldNotTrimWhenBaseUrlOk() {
        Straal.Config config = new Straal.Config("/url/url", Collections.emptyMap(), new DeviceInfo("pl-PL"));
        assertEquals("/url/url", config.merchantBaseUrl);
    }

    @Test
    void shouldAddVersioningHeadersToMerchantHeaders() {
        Straal.Config config = new Straal.Config("/url/url", Collections.emptyMap(), new DeviceInfo("pl-PL"));
        assertTrue(config.merchantApiHeaders.containsKey("x-straal-sdk-version"));
        assertTrue(config.merchantApiHeaders.containsKey("x-straal-sdk-platform"));
    }

    @Test
    void shouldInitConfigWithDefaultCryptKeyEndpoint() {
        Straal.Config config = new Straal.Config("/url", Collections.emptyMap(), new DeviceInfo("pl-PL"));
        assertEquals(Straal.Config.DEFAULT_CRYPT_KEY_ENDPOINT, config.cryptKeyEndpoint);
    }

    @Test
    void shouldInitConfigWithProvidedCryptKeyEndpoint() {
        Straal.Config config = new Straal.Config("/url", "/cryptkeys", Collections.emptyMap(), new DeviceInfo("pl-PL"));
        assertEquals("/cryptkeys", config.cryptKeyEndpoint);
    }
}
