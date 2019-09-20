/*
 * Auth3dsActivity.java
 * Created by Arkadiusz Różalski on 03.09.19
 * Straal SDK for Android
 * Copyright 2018 Straal Sp. z o. o.
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
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.straal.sdk.response.StraalEncrypted3dsResponse;

/**
 * An activity which handles 3D-Secure verification process and based on its outcome or user action (back button) finishes with one of available results: AUTH_3DS_SUCCESS, AUTH_3DS_FAILURE, AUTH_3DS_CANCEL. The result should be handled in onActivityResult method from host activity
 */
public class Auth3dsActivity extends AppCompatActivity implements OnAuth3dsCompleteListener {

    private static String AUTH_3DS_LOCATION_URL_KEY = "auth_3ds_location_url";
    private static String AUTH_3DS_SUCCESS_URL_KEY = "auth_3ds_success_url";
    private static String AUTH_3DS_FAILURE_URL_KEY = "auth_3ds_failure_url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        setContentView(webView);
        String locationUrl = getIntent().getStringExtra(AUTH_3DS_LOCATION_URL_KEY);
        String successUrl = getIntent().getStringExtra(AUTH_3DS_SUCCESS_URL_KEY);
        String failureUrl = getIntent().getStringExtra(AUTH_3DS_FAILURE_URL_KEY);
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
     * Starts auth 3ds activity for result
     * @param activity host activity
     * @param straalResponse response returned from init 3ds operation
     * @param requestCode request code to start activity for result
     */
    public static void startForResult(Activity activity, StraalEncrypted3dsResponse straalResponse, int requestCode) {
        Intent startingIntent = new Intent(activity, Auth3dsActivity.class);
        startingIntent.putExtra(AUTH_3DS_LOCATION_URL_KEY, straalResponse.locationUrl);
        startingIntent.putExtra(AUTH_3DS_SUCCESS_URL_KEY, straalResponse.redirectUrls.successUrl);
        startingIntent.putExtra(AUTH_3DS_FAILURE_URL_KEY, straalResponse.redirectUrls.failureUrl);
        activity.startActivityForResult(startingIntent, requestCode);
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
