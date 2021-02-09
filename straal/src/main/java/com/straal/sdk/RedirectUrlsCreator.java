/*
 * CallbackUrlCreator.java
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

import androidx.annotation.NonNull;

import com.straal.sdk.data.RedirectUrls;

class RedirectUrlsCreator {

    static RedirectUrls createWith(@NonNull String scheme) {
        if (isBlank(scheme)) throw new IllegalArgumentException("Scheme can not be empty or blank");
        return new RedirectUrls(
                String.format(CALLBACK_URL_FORMAT, scheme, CALLBACK_URL_SUCCESS_PATH),
                String.format(CALLBACK_URL_FORMAT, scheme, CALLBACK_URL_FAILURE_PATH)
        );
    }

    private static boolean isBlank(String string) {
        return string.trim().isEmpty();
    }

    private static final String CALLBACK_URL_FORMAT = "%s://sdk.straal.com/x-callback-url/android/%s";
    private static final String CALLBACK_URL_SUCCESS_PATH = "success";
    private static final String CALLBACK_URL_FAILURE_PATH = "failure";
}
