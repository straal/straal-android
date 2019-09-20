/*
 * Auth3dsWebViewClient.java
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

import android.webkit.WebView;
import android.webkit.WebViewClient;

class Auth3dsWebViewClient extends WebViewClient {
    private final OnAuth3dsCompleteListener listener;
    private final String successUrl;
    private final String failureUrl;

    Auth3dsWebViewClient(OnAuth3dsCompleteListener listener, String successUrl, String failureUrl) {
        this.listener = listener;
        this.successUrl = successUrl;
        this.failureUrl = failureUrl;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (url.equals(successUrl)) {
            listener.onSuccess();
        } else if (url.equals(failureUrl)) {
            listener.onFailure();
        }
    }
}
