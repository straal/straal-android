/*
 * MapToJsonCallable.java
 * Created by Konrad Kowalewski on 26.01.18
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
 */

package com.straal.sdk;

import java.util.Map;
import java.util.concurrent.Callable;

class MapToJsonCallable implements Callable<String> {
    private final Callable<Map<String, Object>> dataCallable;

    MapToJsonCallable(Callable<Map<String, Object>> dataCallable) {
        this.dataCallable = dataCallable;
    }

    MapToJsonCallable(Map<String, Object> data) {
        this.dataCallable = SimpleSourceCallable.of(data);
    }

    @Override
    public String call() throws Exception {
        return DataJsoner.toJson(dataCallable.call());
    }
}
