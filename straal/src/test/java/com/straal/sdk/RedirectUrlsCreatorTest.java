/*
 * RedirectUrlsCreatorTest.java
 * Created by Kamil Czanik on 09.02.2021
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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RedirectUrlsCreatorTest {

    @Test
    void testCreatesRedirectUrlsWithProvidedScheme() {
        RedirectUrls expected = new RedirectUrls("xyz.example.app://sdk.straal.com/x-callback-url/android/success", "xyz.example.app://sdk.straal.com/x-callback-url/android/failure");
        RedirectUrls result = RedirectUrlsCreator.createWith("xyz.example.app");
        assertEquals(expected.successUrl, result.successUrl);
        assertEquals(expected.failureUrl, result.failureUrl);
    }

    @Test
    void testThrowsExceptionWhenProvidedSchemeIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            RedirectUrlsCreator.createWith("");
        });
    }

    @Test
    void testThrowsExceptionWhenProvidedSchemeIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            RedirectUrlsCreator.createWith("                ");
        });
    }
}
