/*
 * Straal3DSecure2Params.java
 * Created by Kamil Czanik on 12.01.2021
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

import com.straal.sdk.params.LanguageTag;
import com.straal.sdk.params.Timezone;


public class Straal3DSecure2Params {

    final LanguageTag languageTag;
    final String userAgent;
    final Timezone timezone;
    private static final String USER_AGENT_PROPERTY = "http.agent";

    public Straal3DSecure2Params(LanguageTag languageTag) {
        this.languageTag = languageTag;
        this.timezone = Timezone.getCurrent();
        this.userAgent = System.getProperty(USER_AGENT_PROPERTY);
    }
}
