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

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.straal.sdk.response.StraalEncrypted3dsResponse;
import com.straal.sdk.view.auth3ds.Auth3dsBrowserActivity;

import static com.straal.sdk.view.auth3ds.Auth3dsResult.AUTH_3DS_SUCCESS;

/**
 * Handler which will carry 3DS authentication for you and return final result.
 */
public class Straal3dsTransactionHandler implements Consumer<StraalEncrypted3dsResponse> {

    private ActivityResultLauncher<StraalEncrypted3dsResponse> authenticationLauncher;
    private final Consumer<Integer> onResult;
    private static final String AUTH_RESULT_KEY = "com.straal.sdk.AUTH_RESULT_KEY";

    /**
     * @param lifecycleOwner lifecycleOwner that holds {@link Straal3dsTransactionHandler}
     * @param resultRegistry registry to listen for 3DS authentication results
     * @param onResult       callback which will be invoked with response when operation finishes
     */
    public Straal3dsTransactionHandler(@NonNull LifecycleOwner lifecycleOwner, @NonNull ActivityResultRegistry resultRegistry, @NonNull Consumer<Integer> onResult) {
        this.onResult = onResult;
        registerForAuthenticationResult(lifecycleOwner, resultRegistry);
    }

    /**
     * @see Straal3dsTransactionHandler#Straal3dsTransactionHandler(LifecycleOwner, ActivityResultRegistry, Consumer)
     * @param activity activity that holds {@link Straal3dsTransactionHandler}
     * @param onResult callback which will be invoked with response when operation finishes
     */
    public Straal3dsTransactionHandler(@NonNull ComponentActivity activity, @NonNull Consumer<Integer> onResult) {
        this(activity, activity.getActivityResultRegistry(), onResult);
    }

    @Override
    public void accept(StraalEncrypted3dsResponse response) {
        switch (response.status) {
            case SUCCESS:
                notify(AUTH_3DS_SUCCESS);
                break;
            case CHALLENGE_3DS:
                perform3dsAuthentication(response);
                break;
        }
    }

    private void registerForAuthenticationResult(LifecycleOwner lifecycleOwner, ActivityResultRegistry resultRegistry) {
        authenticationLauncher = resultRegistry.register(AUTH_RESULT_KEY, lifecycleOwner, new Perform3dsAuthentication(), result -> {
            notify(result.getResultCode());
        });
    }

    private void notify(int resultCode) {
        onResult.accept(resultCode);
    }

    private void perform3dsAuthentication(StraalEncrypted3dsResponse response) {
        authenticationLauncher.launch(response);
    }

    private static class Perform3dsAuthentication extends ActivityResultContract<StraalEncrypted3dsResponse, ActivityResult> {

        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, StraalEncrypted3dsResponse input) {
            return Auth3dsBrowserActivity.startingIntent(context, input);
        }

        @Override
        public ActivityResult parseResult(int resultCode, @Nullable Intent intent) {
            return new ActivityResult(resultCode, intent);
        }
    }
}
