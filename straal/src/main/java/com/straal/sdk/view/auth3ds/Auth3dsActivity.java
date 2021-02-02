/*
 * Auth3dsActivity.java
 * Created by Arkadiusz Różalski on 03.09.19
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

package com.straal.sdk.view.auth3ds;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.straal.sdk.response.StraalEncrypted3dsResponse;

/**
 * An activity which handles 3D-Secure verification process and based on its outcome or user action (back button) finishes with one of available results: AUTH_3DS_SUCCESS, AUTH_3DS_FAILURE, AUTH_3DS_CANCEL. The result should be handled in onActivityResult method from host activity
 */
public class Auth3dsActivity extends AppCompatActivity implements OnAuth3dsCompleteListener {

    private final static String AUTH_3DS_LOCATION_URL_KEY = "auth_3ds_location_url";
    private final static String AUTH_3DS_SUCCESS_URL_KEY = "auth_3ds_success_url";
    private final static String AUTH_3DS_FAILURE_URL_KEY = "auth_3ds_failure_url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String locationUrl = getIntent().getStringExtra(AUTH_3DS_LOCATION_URL_KEY);
        String successUrl = getIntent().getStringExtra(AUTH_3DS_SUCCESS_URL_KEY);
        String failureUrl = getIntent().getStringExtra(AUTH_3DS_FAILURE_URL_KEY);
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        setContentView(webView);
        configureWebView(webView, locationUrl, successUrl, failureUrl);
    }

    @Override
    public void onBackPressed() {
        onCancel();
    }

    @Override
    public void onSuccess() {
        finishWithResult(AUTH_3DS_SUCCESS);
    }

    @Override
    public void onFailure() {
        finishWithResult(AUTH_3DS_FAILURE);
    }

    private void onCancel() {
        finishWithResult(AUTH_3DS_CANCEL);
    }

    private void finishWithResult(int resultCode) {
        setResult(resultCode);
        finish();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configureWebView(WebView webView, String locationUrl, String successUrl, String failureUrl) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new Auth3dsWebViewClient(this, successUrl, failureUrl));
        webView.loadUrl(locationUrl);
    }

    /**
     * Creates new starting intent for {@link Auth3dsActivity}
     *
     * @param context        context required to create intent
     * @param straalResponse response returned from init 3ds operation
     */
    @NonNull
    public static Intent startingIntent(@NonNull Context context, @NonNull StraalEncrypted3dsResponse straalResponse) {
        Intent startingIntent = new Intent(context, Auth3dsActivity.class);
        startingIntent.putExtra(AUTH_3DS_LOCATION_URL_KEY, straalResponse.locationUrl);
        startingIntent.putExtra(AUTH_3DS_SUCCESS_URL_KEY, straalResponse.redirectUrls.successUrl);
        startingIntent.putExtra(AUTH_3DS_FAILURE_URL_KEY, straalResponse.redirectUrls.failureUrl);
        return startingIntent;
    }

    /**
     * Authentication 3D-Secure success result
     */
    public static final int AUTH_3DS_SUCCESS = 100;
    /**
     * Authentication 3D-Secure failure result
     */
    public static final int AUTH_3DS_FAILURE = 101;
    /**
     * Authentication 3D-Secure cancel result (e.g. pressed back button)
     */
    public static final int AUTH_3DS_CANCEL = 102;
}
