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
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.straal.sdk.response.StraalEncrypted3dsResponse;

/**
 * Activity that handles 3D-Secure authentication outside application e.g. browser or bank application
 */
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
    /**
     * Authentication 3D-Secure unknown result (e.g. when final url is malformed)
     */
    public static final int AUTH_3DS_RESULT_UNKNOWN = 103;

    private final static String AUTH_3DS_LOCATION_URL_KEY = "com.straal.sdk.AUTH_3DS_LOCATION_URL_KEY";
    private final static String AUTH_3DS_SUCCESS_URL_KEY = "com.straal.sdk.AUTH_3DS_SUCCESS_URL_KEY";
    private final static String AUTH_3DS_FAILURE_URL_KEY = "com.straal.sdk.AUTH_3DS_FAILURE_URL_KEY";
    private Auth3dsParams auth3dsParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        auth3dsParams = createAuth3dsParams(savedInstanceState, intent);
        if (hasLocationUrl(intent) && !isRecreated(savedInstanceState)) perform3dsAuthentication(getLocationUrl(intent));
        else if (hasResult(intent)) captureAuthenticationResult(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(auth3dsParams.writeTo(outState));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        captureAuthenticationResult(intent);
    }

    @Override
    public void onBackPressed() {
        onCancel();
    }

    private Auth3dsParams createAuth3dsParams(Bundle savedState, Intent intent) {
        if (isRecreated(savedState))
            return new Auth3dsParams(savedState);
        else
            return new Auth3dsParams(intent);
    }

    private String getLocationUrl(Intent intent) {
        return intent.getStringExtra(AUTH_3DS_LOCATION_URL_KEY);
    }

    private boolean isRecreated(Bundle bundle) {
        return bundle != null;
    }

    private boolean hasLocationUrl(Intent intent) {
        return intent.hasExtra(AUTH_3DS_LOCATION_URL_KEY);
    }

    private boolean hasResult(Intent intent) {
        return intent.getData() != null;
    }

    private void perform3dsAuthentication(String locationUrl) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(locationUrl)));
    }

    private void captureAuthenticationResult(Intent resultIntent) {
        finish(auth3dsParams.captureResult(resultIntent.getDataString()));
    }

    private void onCancel() {
        finish(AUTH_3DS_RESULT_CANCEL);
    }

    private void finish(int resultCode) {
        setResult(resultCode);
        finish();
    }

    /**
     * Creates new starting intent for {@link Auth3dsBrowserActivity}
     *
     * @param context  context required to create intent
     * @param response response returned from 3ds operation
     */
    @NonNull
    public static Intent startingIntent(@NonNull Context context, @NonNull StraalEncrypted3dsResponse response) {
        Intent startingIntent = new Intent(context, Auth3dsBrowserActivity.class);
        startingIntent.putExtra(AUTH_3DS_LOCATION_URL_KEY, response.locationUrl);
        startingIntent.putExtra(AUTH_3DS_SUCCESS_URL_KEY, response.redirectUrls.successUrl);
        startingIntent.putExtra(AUTH_3DS_FAILURE_URL_KEY, response.redirectUrls.failureUrl);
        return startingIntent;
    }

    private static class Auth3dsParams {

        public final String successUrl;
        public final String failureUrl;

        private Auth3dsParams(String successUrl, String failureUrl) {
            this.successUrl = successUrl;
            this.failureUrl = failureUrl;
        }

        public Auth3dsParams(Bundle bundle) {
            this(bundle.getString(AUTH_3DS_SUCCESS_URL_KEY), bundle.getString(AUTH_3DS_FAILURE_URL_KEY));
        }

        public Auth3dsParams(Intent intent) {
            this(intent.getStringExtra(AUTH_3DS_SUCCESS_URL_KEY), intent.getStringExtra(AUTH_3DS_FAILURE_URL_KEY));
        }

        public int captureResult(String data) {
            if (data == null) return AUTH_3DS_RESULT_UNKNOWN;
            else if (isSuccessUrl(data)) return AUTH_3DS_RESULT_SUCCESS;
            else if (isFailureUrl(data)) return AUTH_3DS_RESULT_FAILURE;
            else return AUTH_3DS_RESULT_UNKNOWN;
        }

        private boolean isSuccessUrl(String data) {
            return data.equals(successUrl);
        }

        private boolean isFailureUrl(String data) {
            return data.equals(failureUrl);
        }

        private Bundle writeTo(Bundle bundle) {
            bundle.putString(AUTH_3DS_SUCCESS_URL_KEY, successUrl);
            bundle.putString(AUTH_3DS_FAILURE_URL_KEY, failureUrl);
            return bundle;
        }
    }
}
