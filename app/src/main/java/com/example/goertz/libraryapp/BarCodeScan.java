package com.example.goertz.libraryapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BarCodeScan extends ActionBarActivity {

    private final String LOG_TAG = BarCodeScan.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_scan);

        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            // String scanFormat = scanningResult.getFormatName();
            Log.e(LOG_TAG,"gescannt: "+scanContent);
            DatabaseRequest(scanContent);
            //Toast toast = Toast.makeText(getApplicationContext(),
             //       scanContent, Toast.LENGTH_SHORT);
            //toast.show();

        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }
    private void DatabaseRequest(String BarCode) {

        // Send the feedback to the given url
        String url = "http://libapp.byparamatma.com/appBarCode.php";

        // Build get parameters
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
        nameValuePair.add(new BasicNameValuePair("recipient", "app"));
        nameValuePair.add(new BasicNameValuePair("barcode", BarCode));

        SendBarCodeTask myTask = new SendBarCodeTask(this, nameValuePair);

        // URL must be encoded because it can't contain white spaces etc.
        try {
            String encoded = URLEncoder.encode(url, "utf-8");
            myTask.execute(new String[]{encoded});
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "Error while processing the Message.", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bar_code_scan, menu);
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

    public class SendBarCodeTask extends AsyncTask<String, Void, String> {
        /**
         * progress dialog to show user that the sending is processing.
         */
        private ProgressDialog dialog;
        /**
         * The context of this task *
         */
        private Context context;
        /**
         * Parameters for the URL *
         */
        private List<NameValuePair> parameters;

        /**
         * Constructor
         * <p/>
         * Initializes the progress dialog and sets the context and the parameters.
         */
        public SendBarCodeTask(Context context, List<NameValuePair> parameters) {
            this.context = context;
            dialog = new ProgressDialog(context);
            this.parameters = parameters;
        }

        /**
         * Show the progress dialog if the device is online before the task is executed.
         */
        protected void onPreExecute() {
            if (isOnline()) {
                this.dialog.show();
                this.dialog.setMessage("Processing BarCode....");
            }
        }

        @Override
        protected void onPostExecute(final String success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }


            Intent intent = new Intent(getApplicationContext(), BookDetails.class);
            intent.putExtra(BibScan.EXTRA_MESSAGE, success);
            startActivity(intent);


            // Log.v(LOG_TAG, "RESULT " + success);
            //mTextView.setText(success);
        }

        protected String doInBackground(final String... urls) {
            if (isOnline()) {
                String response = executeHttpGet(urls[0]);
                Log.v(LOG_TAG, "RESULT " + response);
                if (response != null) {
                    return response;
                }
            }

            return null;
        }


        private String executeHttpGet(String url) {
            String response = null;

            // Initialize HttpClient and HttpPost
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://libapp.byparamatma.com/appBarCode.php");

            try {
                httppost.setEntity(new UrlEncodedFormEntity(parameters));

                // Execute HTTP post request and get response as string
                HttpResponse httpresponse = httpclient.execute(httppost);
                HttpEntity entity = httpresponse.getEntity();
                response = EntityUtils.toString(entity);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        public boolean isOnline() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;

        }
    }
}
