/*
 * KeyResponse.java
 * Created by Arkadiusz Różalski on 26.01.18
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

package com.straal.sdk.response;

public class KeyResponse {
    public final String id;
    public final String permission;
    public final String key;
    public final int ttl;
    public final long createdAt;

    public KeyResponse(String id, String permission, String key, int ttl, long createdAt) {
        this.createdAt = createdAt;
        this.ttl = ttl;
        this.id = id;
        this.key = key;
        this.permission = permission;
    }
}
