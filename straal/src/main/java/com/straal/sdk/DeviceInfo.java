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

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Class for device specific data required to carry 3D-Secure verification
 *
 * @see <a href="https://api-reference.straal.com/#resources-transactions-3ds-v2-request-parameters">'Create transaction' with card in Straal API docs</a>
 */
public class DeviceInfo {

    final String languageTag;
    final String userAgent;
    final long timezoneOffset;
    private static final String USER_AGENT_PROPERTY = "http.agent";

    /**
     * @param languageTag device language tag in IETF BCP 47 format
     */
    public DeviceInfo(String languageTag) {
        this.languageTag = languageTag;
        this.timezoneOffset = timezoneOffset();
        this.userAgent = System.getProperty(USER_AGENT_PROPERTY);
    }

    private long timezoneOffset() {
        return TimeUnit.MILLISECONDS.toMinutes(Calendar.getInstance().getTimeZone().getRawOffset());
    }
}
