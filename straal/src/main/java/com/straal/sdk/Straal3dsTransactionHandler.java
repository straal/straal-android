/*
 * Straal3dsTransactionHandler.java
 * Created by Kamil Czanik on 29.01.2021
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

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.straal.sdk.response.StraalEncrypted3dsResponse;
import com.straal.sdk.response.TransactionStatus;
import com.straal.sdk.view.auth3ds.Auth3dsActivity;

public class Straal3dsTransactionHandler implements Consumer<StraalEncrypted3dsResponse> {

    private ActivityResultLauncher<Intent> resultLauncher;
    private final Context context;
    private final Consumer<Integer> resultConsumer;
    private final ActivityResultRegistry resultRegistry;
    private static final String AUTH_RESULT_KEY = "com.straal.sdk.AUTH_RESULT_KEY";

    public Straal3dsTransactionHandler(@NonNull Context context, @NonNull ActivityResultRegistry resultRegistry, @NonNull Consumer<Integer> resultConsumer) {
        this.context = context;
        this.resultRegistry = resultRegistry;
        this.resultConsumer = resultConsumer;
    }

    public void attachTo(@NonNull LifecycleOwner owner) {
        registerForResult(owner, resultRegistry);
    }

    @NonNull
    private void registerForResult(LifecycleOwner lifecycleOwner, ActivityResultRegistry resultRegistry) {
        resultLauncher = resultRegistry.register(AUTH_RESULT_KEY, lifecycleOwner, new ActivityResultContracts.StartActivityForResult(), result -> {
            notify(result.getResultCode());
        });
    }

    @Override
    public void accept(StraalEncrypted3dsResponse response) {
        if (response.status == TransactionStatus.SUCCESS) notify(Auth3dsActivity.RESULT_OK);
        else if (response.status == TransactionStatus.CHALLENGE_3DS) start3dsChallenge(response);
    }

    private void notify(int resultCode) {
        resultConsumer.accept(resultCode);
    }

    private void start3dsChallenge(StraalEncrypted3dsResponse response) {
        runOnMainThread(() -> resultLauncher.launch(Auth3dsActivity.getIntent(context, response)));
    }

    private void runOnMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
