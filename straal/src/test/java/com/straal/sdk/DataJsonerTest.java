/*
 * DataJsonerTest.java
 * Created by Konrad Kowalewski on 26.01.18
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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.straal.sdk.DataJsoner.toJson;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("DataJsoner")
class DataJsonerTest {
    @Test
    void shouldProperlySerializeSimpleMap() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("a", 1);
        data.put("b", true);
        data.put("c", 2.5f);
        data.put("d", "xyz");
        assertEquals("{\"a\":1,\"b\":true,\"c\":2.5,\"d\":\"xyz\"}", toJson(data));
    }

    @Test
    void shouldProperlySerializeMapWithCollection() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("a", Arrays.asList("abc", "xyz"));
        data.put("b", Arrays.asList(1, 2, 3));
        data.put("c", new int[]{1, 2, 3});
        assertEquals("{\"a\":[\"abc\",\"xyz\"],\"b\":[1,2,3],\"c\":[1,2,3]}", toJson(data));
    }

    @Test
    void shouldProperlySerializeMapWithMap() throws Exception {
        Map<String, Object> data = new HashMap<>();
        HashMap<String, Object> innerData = new HashMap<>();
        innerData.put("a", 1);
        innerData.put("b", true);
        data.put("a", innerData);
        assertEquals("{\"a\":{\"a\":1,\"b\":true}}", toJson(data));
    }

    @Test
    void shouldProperlySerializeMapWithCollectionOfMaps() throws Exception {
        Map<String, Object> data = new HashMap<>();
        HashMap<String, Object> innerData1 = new HashMap<>();
        HashMap<String, Object> innerData2 = new HashMap<>();
        innerData1.put("a", 1);
        innerData2.put("b", true);
        data.put("a", Arrays.asList(innerData1, innerData2));
        assertEquals("{\"a\":[{\"a\":1},{\"b\":true}]}", toJson(data));
    }
}
