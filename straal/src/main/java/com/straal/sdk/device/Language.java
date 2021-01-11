/*
 * Language.java
 * Created by Kamil Czanik on 11.01.2021
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

package com.straal.sdk.device;

import java.util.Locale;

public class Language {

    public final String tag;

    private Language(Locale locale) {
        this.tag = "pl-PL";
    }

    public static Language getDefault() {
        return new Language(Locale.getDefault());
    }
}
