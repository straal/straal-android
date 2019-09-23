/*
 * EncryptCallableTest.java
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

import com.straal.sdk.exceptions.EncryptionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("EncryptCallable")
class EncryptCallableTest {
    private EncryptCallable encryptCallable;
    private StraalCrypterCallable crypterCallable = mock(StraalCrypterCallable.class);
    private MapToJsonCallable mapToJsonCallable = mock(MapToJsonCallable.class);
    private String data = "{\"number\":\"4444444444444448\",\"name\":\"User Name\",\"expiry_year\":2018,\"expiry_month\":12,\"cvv\":\"444\"}";

    @BeforeEach
    void setup() throws Exception {
        when(mapToJsonCallable.call()).thenReturn(data);
    }

    @DisplayName("should return encrypted data")
    @Test
    void crypt() throws Exception {
        StraalCrypter crypter = new StraalCrypter("0c228549af800019792f7e15654643bc1924de7d849f16dfc3a157b575adc41b33a478579135f9d7d7ce159c3d16141d066004e13b8f885e769b792b0a820049bbde751cbe310f36");
        when(crypterCallable.call()).thenReturn(crypter);
        encryptCallable = createEncryptCallable();
        String encryptedData = encryptCallable.call();
        assertNotEquals(data, encryptedData);

    }

    @DisplayName("should throw encryption exception")
    @Test
    void throwException() throws Exception {
        StraalCrypter crypter = mock(StraalCrypter.class);
        when(crypter.encryptString(any())).thenThrow(EncryptionException.class);
        when(crypterCallable.call()).thenReturn(crypter);
        encryptCallable = createEncryptCallable();
        assertThrows(EncryptionException.class, encryptCallable::call);

    }

    private EncryptCallable createEncryptCallable() {
        return new EncryptCallable(crypterCallable, mapToJsonCallable);
    }
}
