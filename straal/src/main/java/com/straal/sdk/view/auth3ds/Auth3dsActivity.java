package com.straal.sdk.view.auth3ds;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.straal.sdk.response.Auth3dsContext;

public class Auth3dsActivity extends Activity implements OnAuth3dsCompleteListener {

    private static String AUTH_3DS_CONTEXT_KEY = "auth_3ds_context";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        setContentView(webView);
        Auth3dsContext auth3dsContext = (Auth3dsContext) getIntent().getSerializableExtra(AUTH_3DS_CONTEXT_KEY);
        configureWebView(webView, auth3dsContext);
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
    private void configureWebView(WebView webView, Auth3dsContext auth3dsContext) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new Auth3dsWebViewClient(this, auth3dsContext.successUrl, auth3dsContext.failureUrl));
        webView.loadUrl(auth3dsContext.locationUrl);
    }

    public static void startForResult(Activity activity, Auth3dsContext auth3dsContext, int requestCode) {
        Intent startingIntent = new Intent(activity, Auth3dsActivity.class);
        startingIntent.putExtra(AUTH_3DS_CONTEXT_KEY, auth3dsContext);
        activity.startActivityForResult(startingIntent, requestCode);
    }

    public static final int AUTH_3DS_SUCCESS = 100;
    public static final int AUTH_3DS_FAILURE = 101;
    public static final int AUTH_3DS_CANCEL = 102;
}
