/*
 * Straal.java
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

import androidx.annotation.NonNull;

import com.straal.sdk.exceptions.StraalException;
import com.straal.sdk.response.StraalResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Use this class to perform Straal operations.
 * Objects of this type are immutable by design. If you need to change configuration, simply create a new object.
 * You can create as many objects of this type as you want.
 */
public final class Straal {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Config config;

    /**
     * Initializes Straal SDK with supplied {@link Config}.
     *
     * @param config {@link Config} containing your application backend parameters
     */
    public Straal(@NonNull Config config) {
        this.config = config;
    }

    /**
     * Performs supplied {@link StraalOperation} on the calling thread.
     * Make sure to never call this method from the applications main thread.
     * Check out the {@link #performAsync(StraalOperation, Consumer, Consumer)  asynchronous} version of this method if it is more suitable for your case.
     *
     * @param operation operation to be performed
     * @param <T>       subtype of {@link StraalResponse} that is associated with supplied {@code operation}
     * @return response of type T with status code and additional data (associated with performed operation) if operation was successful
     * @throws StraalException if operation was unsuccessful. Check exception's cause for details
     * @see #performAsync(StraalOperation, Consumer, Consumer)
     */
    public <T extends StraalResponse> T perform(StraalOperation<T> operation) throws StraalException {
        try {
            return operation.perform(config);
        } catch (Exception e) {
            throw new StraalException(e);
        }
    }

    /**
     * Performs supplied {@link StraalOperation} on another thread. Internally an Executor's single thread is used.
     * Check out the {@link #perform(StraalOperation) synchronous} version of this method if it is more suitable for your case.
     *
     * @param operation operation to be performed
     * @param onSuccess callback which will be invoked with response of type T when operation succeeds
     * @param onFailure callback which will be invoked with {@link StraalException} when operation fails
     * @param <T>       subtype of {@link StraalResponse} that is associated with supplied {@code operation}
     * @see #perform(StraalOperation)
     */
    public <T extends StraalResponse> void performAsync(StraalOperation<T> operation, Consumer<T> onSuccess, Consumer<StraalException> onFailure) {
        Runnable runnable = new OperationRunnable<>(operation, config, onSuccess, onFailure);
        executor.execute(runnable);
    }

    /**
     * Used to initialize Straal SDK with parameters defining backend service of your application.
     * See {@link #Config(String, Map, DeviceInfo) constructor} for details.
     *
     * @see Straal#Straal(Config config)
     */
    public static class Config {
        static final String DEFAULT_CRYPT_KEY_ENDPOINT = "/straal/v1/cryptkeys";
        static final String API_BASE_URL = "https://api.straal.com/v1";
        static final Map<String, String> API_HTTP_HEADERS = versioningHeaders();
        static final String PLATFORM = "android";
        public final String merchantBaseUrl;
        public final String cryptKeyEndpoint;
        public final Map<String, String> merchantApiHeaders;
        public final DeviceInfo deviceInfo;

        /**
         * @param merchantBaseUrl        base URL (without trailing slash) of your backend service that uses Straal SDK and provides crypt key endpoint
         * @param merchantRequestHeaders map of key-value pairs that will be added as headers to every HTTP request to your backend service
         * @param deviceInfo             device data required to carry 3D-Secure transaction
         */
        public Config(String merchantBaseUrl, Map<String, String> merchantRequestHeaders, @NonNull DeviceInfo deviceInfo) {
            this(merchantBaseUrl, DEFAULT_CRYPT_KEY_ENDPOINT, merchantRequestHeaders, deviceInfo);
        }

        /**
         * @param merchantBaseUrl        base URL (without trailing slash) of your backend service that uses Straal SDK and provides crypt key endpoint
         * @param cryptKeyEndpoint       crypt key endpoint (with initial slash) of your backend service that uses Straal SDK
         * @param merchantRequestHeaders map of key-value pairs that will be added as headers to every HTTP request to your backend service
         * @param deviceInfo             device data required to carry 3D-Secure transaction
         */
        public Config(String merchantBaseUrl, String cryptKeyEndpoint, Map<String, String> merchantRequestHeaders, @NonNull DeviceInfo deviceInfo) {
            this.merchantBaseUrl = trimTrailingSlashes(merchantBaseUrl);
            this.merchantApiHeaders = new HashMap<>(merchantRequestHeaders);
            this.merchantApiHeaders.putAll(versioningHeaders());
            this.cryptKeyEndpoint = cryptKeyEndpoint;
            this.deviceInfo = deviceInfo;
        }

        private static String trimTrailingSlashes(String text) {
            return text.replaceAll("/*$", "");
        }

        private static Map<String, String> versioningHeaders() {
            Map<String, String> map = new HashMap<>();
            map.put("x-straal-sdk-version", BuildConfig.VERSION_NAME);
            map.put("x-straal-sdk-platform", PLATFORM);
            return map;
        }
    }
}
