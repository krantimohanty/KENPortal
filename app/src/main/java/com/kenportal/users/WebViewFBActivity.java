package com.kenportal.users;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.kenportal.users.urls.ServerLinks;

public class WebViewFBActivity extends AppCompatActivity {
    Toolbar toolbar;
    WebView browser;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_fb);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Toolbar customization
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loading=(ProgressBar)findViewById(R.id.loading);
        browser = (WebView) findViewById(R.id.webview);
        browser.setWebViewClient(new MyBrowser());

        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);

        browser.loadUrl(ServerLinks.FBWebViewUrl);
    }

    private class MyBrowser extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            loading.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            loading.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    loading.setVisibility(View.GONE);
                }
            }, 5000);

            super.onPageFinished(view, url);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {

             if( browser.canGoBack()){
                 browser.goBack();
            }else{
                 finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if( browser.canGoBack()){
            browser.goBack();
        }else{
            finish();
        }

    }
}
