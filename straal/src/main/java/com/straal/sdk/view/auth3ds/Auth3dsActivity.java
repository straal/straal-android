package com.straal.sdk.view.auth3ds;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.straal.sdk.response.Auth3dsContext;

/**
 * An activity which handles 3D-Secure verification process and based on its outcome or user action (back button) finishes with one of available results: AUTH_3DS_SUCCESS, AUTH_3DS_FAILURE, AUTH_3DS_CANCEL. The result should be handled in onActivityResult method from host activity
 */
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

    /**
     * Starts auth 3ds activity for result
     * @param activity host activity
     * @param auth3dsContext data class which contains redirect urls to complete the 3D-Secure verification process
     * @param requestCode request code to start activity for result
     */
    public static void startForResult(Activity activity, Auth3dsContext auth3dsContext, int requestCode) {
        Intent startingIntent = new Intent(activity, Auth3dsActivity.class);
        startingIntent.putExtra(AUTH_3DS_CONTEXT_KEY, auth3dsContext);
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
