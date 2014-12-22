package com.example.goertz.libraryapp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;


public class KalenderActivity extends ActionBarActivity {

    /** The WebView which will display the web site. **/
    private WebView myWebView;
    /** Dialog that indicates that the web site is loading. **/
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalender);

        // Show the back button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get map of extended data from Intent as a Bundle
        Bundle bundle = getIntent().getExtras();

        // The title for the Activity is passed by an Intent
        setTitle(bundle.getString("title"));
        myWebView = (WebView) findViewById(R.id.webView);

        // Create progress dialog which will be shown while loading the url
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);

        // WebView settings
        WebSettings myWebSettings = myWebView.getSettings();
        myWebSettings.setJavaScriptEnabled(true);
        myWebSettings.setDomStorageEnabled(true);
        myWebView.setWebViewClient(new Callback());

        // When the calendar is shown, the user shouldn't be able to zoom in the WebView
        if (!getTitle().equals("Schedule Plan")) {
            myWebSettings.setBuiltInZoomControls(true);
        }

        // Allow cookies in the WebView
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.acceptCookie();

        new Connection();
        // Load the URL given to the WebView
        myWebView.loadUrl(bundle.getString("url"));

    }

    private class Callback extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // Show the loading dialog when the page starts loading
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // Just load the new URL in the current WebView
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // Dismiss the loading dialog
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        @Override
        public void onReceivedError (WebView view, int errorCode, String description, String failingUrl) {
            // Show custom error page when page could not be loaded
            view.loadUrl("file:///android_asset/error/error.html");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kalender, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class Connection extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            connect();
            return null;
        }

    }

    private void connect() {
            String username = "wi14m056";
            String host = "cis.technikum-wien.at";
            String password = "Aghisi10";

            String urlBasePath = "http://" + username + ".cis.technikum-wien.at/cis/index_login.php";
            String urlApiCall_FindAllRepositories = urlBasePath
                    + "repositories.xml";

            try {
                HttpClient client = new DefaultHttpClient();

                AuthScope as = new AuthScope(host, 443);
                UsernamePasswordCredentials upc = new UsernamePasswordCredentials(
                        username, password);

                ((AbstractHttpClient) client).getCredentialsProvider()
                        .setCredentials(as, upc);

                BasicHttpContext localContext = new BasicHttpContext();

                BasicScheme basicAuth = new BasicScheme();
                localContext.setAttribute("preemptive-auth", basicAuth);

                HttpHost targetHost = new HttpHost(host, 443, "https");

                HttpGet httpget = new HttpGet(urlApiCall_FindAllRepositories);
                httpget.setHeader("Content-Type", "application/xml");

                HttpResponse response = client.execute(targetHost, httpget,
                        localContext);

                HttpEntity entity = response.getEntity();
                Object content = EntityUtils.toString(entity);

                Log.d("TEST", "OK: " + content.toString());

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TESt", "Error: " + e.getMessage());
            }
    }
}
