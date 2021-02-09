/*
 * Auth3dsResult.java
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

package com.straal.sdk.view.auth3ds;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class Auth3dsResult {

    /**
     * Authentication 3D-Secure success result
     */
    public static final int AUTH_3DS_SUCCESS = RESULT_OK;
    /**
     * Authentication 3D-Secure failure result
     */
    public static final int AUTH_3DS_FAILURE = 101;
    /**
     * Authentication 3D-Secure cancel result (e.g. pressed back button)
     */
    public static final int AUTH_3DS_CANCEL = RESULT_CANCELED;
    /**
     * Authentication 3D-Secure unknown result (e.g. when final url is malformed)
     */
    public static final int AUTH_3DS_UNKNOWN = 103; //Todo find new name
}
