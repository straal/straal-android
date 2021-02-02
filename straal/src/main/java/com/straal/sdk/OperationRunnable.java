/*
 * OperationRunnable.java
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

import com.straal.sdk.exceptions.StraalException;
import com.straal.sdk.response.StraalResponse;

class OperationRunnable<T extends StraalResponse> implements Runnable {
    private final StraalOperation<T> operation;
    private final Straal.Config config;
    private final Consumer<T> onSuccessListener;
    private final Consumer<StraalException> onFailureListener;

    OperationRunnable(StraalOperation<T> operation, Straal.Config config, Consumer<T> onSuccess, Consumer<StraalException> onFailure) {
        this.operation = operation;
        this.config = config;
        this.onSuccessListener = onSuccess;
        this.onFailureListener = onFailure;
    }

    @Override
    public void run() {
        try {
            T response = operation.perform(config);
            onSuccessListener.accept(response);
        } catch (Exception e) {
            onFailureListener.accept(new StraalException(e));
        }
    }
}
