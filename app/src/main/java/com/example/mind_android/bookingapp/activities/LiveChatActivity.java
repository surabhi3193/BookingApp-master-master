package com.example.mind_android.bookingapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.example.mind_android.bookingapp.R;

public class LiveChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chat);

        ImageView back_btn = findViewById(R.id.back_btn);
        WebView webView = findViewById(R.id.webView);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();

            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new TestWebViewClient());
        webView.setWebViewClient(new TestWebViewClient());

        webView.loadUrl("http://mindlbs.com/livezilla/chat.php");
    }
}
