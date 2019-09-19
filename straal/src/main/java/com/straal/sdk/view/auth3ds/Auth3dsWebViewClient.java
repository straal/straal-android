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
            return;
        }
        if (url.equals(failureUrl)) {
            listener.onFailure();
        }
    }
}
