package com.kenportal.users;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.ConnectionDetector;
import com.kenportal.users.utils.CustomPreference;
import com.kenportal.users.utils.EncodingUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class AttendanceActivity extends AppCompatActivity {

    private WebView webview;

    ConnectionDetector cd;
    LinearLayout loader;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Attendance Report");
        toolbar.setTitleTextColor(Color.WHITE);

        loader = (LinearLayout) findViewById(R.id.loader);
        /*progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setMax(100);*/
        cd = new ConnectionDetector(AttendanceActivity.this);

        String pwd = CustomPreference.with(AttendanceActivity.this).getString("password", "");
        String username = CustomPreference.with(AttendanceActivity.this).getString("username", "");
        String device_id = CustomPreference.with(AttendanceActivity.this).getString("device_id", "");

        String postData = "strUserId=" + username + "&strPwd=" + pwd + "&strbrowser=Android_Mob&strDeviceId=" + device_id + "&strPath=" + "DashboardStandardv1/attendanceReport.aspx";
        Log.d("atag", postData);
        byte[] post = EncodingUtils.getBytes(postData, "BASE64");


        webview = (WebView) findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        //webview.setWebChromeClient(new WebChromeClient());
        //webview.setWebChromeClient(new MyWebViewClient());
        final MyJavaScriptInterface myJavaScriptInterface = new MyJavaScriptInterface(this);
        webview.addJavascriptInterface(myJavaScriptInterface,"activity");
        webview.postUrl(ServerLinks.portalUrl + "/mobileappSession.aspx", post);
        isInternetPresent = cd.isConnectingToInternet();
        if (cd.isConnectingToInternet()) {
            //final ProgressDialog progressDialog = new ProgressDialog(WebViewActivity.this);
            webview.setWebChromeClient(new WebChromeClient() {

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.d("atag", url);
                    view.loadUrl(url);
                    //WebViewActivity.this.progress.setProgress(0);
                    return true;
                }

                @SuppressWarnings("deprecation")
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    // Handle the error
                    Log.d("atag", "Error: " + description);
                }

                @TargetApi(Build.VERSION_CODES.M)
                public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                    // Redirect to deprecated method, so you can use it in all SDK versions
                    Log.d("atag", "Error: " + rerr.getDescription().toString());
                    onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());

                }
//                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                    //webview.loadUrl("");
//                    // l.setVisibility(View.VISIBLE);
//                 /*   try_again.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            webview.reload();
//                            l.setVisibility(View.GONE);
//                        }
//                    });*/
//                }


                public void onPageFinished(WebView view, String url) {
                    // super.onPageFinished(view, url);
                    //progressDialog.dismiss();
                    loader.setVisibility(View.GONE);
                }


                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    // TODO Auto-generated method stub
                    // super.onPageStarted(view, url, favicon);
                   /* progressDialog.setMessage("Loading ...");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();*/
                }

            });
            //Loading Url
            //loadURL();
        } else {


        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /*private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //WebViewActivity.this.setValue(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }*/

    /*public void setValue(int progress) {
        this.progress.setProgress(progress);
    }*/


    public void loadURL() {
       /* webview.clearCache(true);
        webview.clearView();*/

        Log.d("URL",ServerLinks.AttendanceWebViewUrl);

        //Load url
        if (webview.getUrl() == null) {
            webview.loadUrl(ServerLinks.AttendanceWebViewUrl); // change to live url
        }
    }

    public void onBackPressed() {

        if (webview != null) {
            if (webview.canGoBack()) {
                webview.goBack();
            } else {
                super.onBackPressed();
            }
        } else
            finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //startActivity(new Intent(WebViewActivity.this, ActionActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "WebView Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.kenportal.users/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "WebView Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.kenportal.users/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class MyJavaScriptInterface {
        Context mContext;

        MyJavaScriptInterface(Context c) {
            mContext = c;
        }

        //Add @JavascriptInterface to call this method from > 4.2 Android Version
        @JavascriptInterface
        public void TestMethod() {
            startActivity(new Intent(AttendanceActivity.this, ActionActivity.class));
            //context.startActivity(new Intent(context, WebViewActivity.class).putExtra("url", getUrl()).putExtra("sub", getSubject()));
            //Toast.makeText(mContext, "Hello from JavaScript Interface", Toast.LENGTH_SHORT).show();
        }
    }
}
