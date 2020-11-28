package com.kulkarni.mimoh.smartstudy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebView_Activity extends Activity{

    private String url;
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
        }
        else startActivity(new Intent(this, Homepage_Activity.class));

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if (!webView.getUrl().equals(url)){
                    webView.loadUrl(url);
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (!webView.getUrl().equals(url)){
                    webView.loadUrl(url);
                }
                super.onPageStarted(view, url, favicon);
            }
        });
    }

    @Override
    public void onBackPressed() {
        webView.destroy();
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        webView.destroy();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        startActivity(new Intent(WebView_Activity.this,Homepage_Activity.class));
        super.onRestart();
    }
}