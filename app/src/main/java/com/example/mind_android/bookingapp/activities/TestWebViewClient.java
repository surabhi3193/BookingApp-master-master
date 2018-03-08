package com.example.mind_android.bookingapp.activities;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TestWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Log.i("WEB_VIEW_TEST", "error code:" + errorCode);
        super.onReceivedError(view, errorCode, description, failingUrl);
    }
}

