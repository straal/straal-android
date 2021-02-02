/*
 * Auth3dsBrowserActivity.java
 * Created by Kamil Czanik on 02.02.2021
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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.straal.sdk.response.StraalEncrypted3dsResponse;

public class Auth3dsBrowserActivity extends AppCompatActivity {

    /**
     * Authentication 3D-Secure success result
     */
    public static final int AUTH_3DS_RESULT_SUCCESS = 100;
    /**
     * Authentication 3D-Secure failure result
     */
    public static final int AUTH_3DS_RESULT_FAILURE = 101;
    /**
     * Authentication 3D-Secure cancel result (e.g. pressed back button)
     */
    public static final int AUTH_3DS_RESULT_CANCEL = 102;

    private final static String AUTH_3DS_LOCATION_URL_KEY = "com.straal.sdk.AUTH_3DS_LOCATION_URL_KEY";
    private final static String AUTH_3DS_SUCCESS_URL_KEY = "com.straal.sdk.AUTH_3DS_SUCCESS_URL_KEY";
    private final static String AUTH_3DS_FAILURE_URL_KEY = "com.straal.sdk.AUTH_3DS_FAILURE_URL_KEY";
    private Storage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = new Storage(this);
        if (shouldCaptureAuthenticationResult()) {
            captureAuthenticationResult(getIntent());
        } else if (shouldStartAuthentication()) {
            beginAuthentication();
        }
    }

    private boolean shouldCaptureAuthenticationResult() {
        return storage.hasStoredCallbackUrls() && isNotRecreated();
    }

    private boolean isNotRecreated() {
        return !storage.getLocationUrl().equals(getLocationUrl());
    }

    private boolean shouldStartAuthentication() {
        return !storage.hasStoredCallbackUrls();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        captureAuthenticationResult(intent);
    }

    private void beginAuthentication() {
        storage.store(getLocationUrl(), getSuccessUrl(), getFailureUrl());
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getLocationUrl())));
    }

    private void captureAuthenticationResult(Intent resultIntent) {
        String data = resultIntent.getDataString();
        if (data.equals(storage.getSuccessUrl())) {
            onSuccess();
            storage.clear();
        } else if (data.equals(storage.getFailureUrl())) {
            onFailure();
            storage.clear();
        } else throw new IllegalArgumentException("Could not recognize captured callback url");
    }

    private String getLocationUrl() {
        return getIntent().getStringExtra(AUTH_3DS_LOCATION_URL_KEY);
    }

    private String getSuccessUrl() {
        return getIntent().getStringExtra(AUTH_3DS_SUCCESS_URL_KEY);
    }

    private String getFailureUrl() {
        return getIntent().getStringExtra(AUTH_3DS_FAILURE_URL_KEY);
    }

    @Override
    public void onBackPressed() {
        onCancel();
    }

    private void onSuccess() {
        finishWithResult(AUTH_3DS_RESULT_SUCCESS);
    }

    private void onFailure() {
        finishWithResult(AUTH_3DS_RESULT_FAILURE);
    }

    private void onCancel() {
        finishWithResult(AUTH_3DS_RESULT_CANCEL);
    }

    private void finishWithResult(int resultCode) {
        setResult(resultCode);
        finish();
    }

    /**
     * Creates new starting intent for {@link Auth3dsActivity}
     *
     * @param context  context required to create intent
     * @param response response returned from init 3ds operation
     */
    @NonNull
    public static Intent startingIntent(@NonNull Context context, @NonNull StraalEncrypted3dsResponse response) {
        Intent startingIntent = new Intent(context, Auth3dsBrowserActivity.class);
        startingIntent.putExtra(AUTH_3DS_LOCATION_URL_KEY, response.locationUrl);
        startingIntent.putExtra(AUTH_3DS_SUCCESS_URL_KEY, response.redirectUrls.successUrl);
        startingIntent.putExtra(AUTH_3DS_FAILURE_URL_KEY, response.redirectUrls.failureUrl);
        return startingIntent;
    }

    private static class Storage {

        private static final String URL_CALLBACKS_PREFS_KEY = "com.straal.sdk.URL_CALLBACKS_PREFS_KEY";
        private final SharedPreferences preferences;

        Storage(Context context) {
            preferences = context.getSharedPreferences(URL_CALLBACKS_PREFS_KEY, Context.MODE_PRIVATE);
        }

        void store(String locationUrl, String successUrl, String failureUrl) {
            preferences.edit()
                    .putString(AUTH_3DS_LOCATION_URL_KEY, locationUrl)
                    .putString(AUTH_3DS_SUCCESS_URL_KEY, successUrl)
                    .putString(AUTH_3DS_FAILURE_URL_KEY, failureUrl)
                    .apply();
        }

        String getLocationUrl() {
            return readString(AUTH_3DS_LOCATION_URL_KEY);
        }

        String getSuccessUrl() {
            return readString(AUTH_3DS_SUCCESS_URL_KEY);
        }

        String getFailureUrl() {
            return readString(AUTH_3DS_FAILURE_URL_KEY);
        }

        boolean hasStoredCallbackUrls() {
            return preferences.contains(AUTH_3DS_LOCATION_URL_KEY)
                    && preferences.contains(AUTH_3DS_SUCCESS_URL_KEY)
                    && preferences.contains(AUTH_3DS_FAILURE_URL_KEY);
        }

        void clear() {
            preferences.edit().clear().apply();
        }

        private String readString(String key) {
            return preferences.getString(key, null);
        }
    }
}
