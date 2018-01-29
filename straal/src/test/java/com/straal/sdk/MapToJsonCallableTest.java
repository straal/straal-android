/*
 * MapToJsonCallableTest.java
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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@DisplayName("MapToJsonCallable")
class MapToJsonCallableTest {

    @DisplayName("should return json")
    @Test
    void returnJson() throws Exception {
        String expectedJson = "{\"a\":\"test\",\"b\":1}";
        Map<String, Object> sampleMap = new HashMap<>();
        sampleMap.put("a", "test");
        sampleMap.put("b", 1);
        String output = new MapToJsonCallable(sampleMap).call();
        assertEquals(expectedJson, output);
    }
}
