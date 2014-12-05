package com.example.goertz.libraryapp;


import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.List;


public class BibScan extends Activity {
    private final String LOG_TAG = BibScan.class.getSimpleName();
    public final static String EXTRA_MESSAGE = "MESSAGE";
    private TextView mTextView;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.activity_bib_scan);
        mTextView = (TextView) findViewById(R.id.info);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter != null) {
            mTextView.setText("Please hold the book behind your phone to scan the NFC Chip.");
        } else {
            mTextView.setText("This phone is not NFC enabled.");
        }

        // create an intent with tag data and deliver to this activity
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[]{ndefIntent};
        } catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }

        mNFCTechLists = new String[][]{new String[]{NfcF.class.getName()}};
    }

    @Override
    public void onNewIntent(Intent intent) {
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String s = "";

        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (data != null) {
            try {
                for (int i = 0; i < data.length; i++) {
                    NdefRecord[] recs = ((NdefMessage) data[i]).getRecords();
                    for (int j = 0; j < recs.length; j++) {
                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {
                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;

                            s = new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1,
                                    textEncoding);
                            DatabaseRequest(s);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }
        }

        mTextView.setText(s);
    }

    private void DatabaseRequest(String nfctag) {

        // Send the feedback to the given url
        String url = "http://libapp.byparamatma.com/appNFC.php";

        // Build get parameters
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
        nameValuePair.add(new BasicNameValuePair("recipient", "app"));
        nameValuePair.add(new BasicNameValuePair("nfctag", nfctag));

        SendNFCTask myTask = new SendNFCTask(this, nameValuePair);

        // URL must be encoded because it can't contain white spaces etc.
        try {
            String encoded = URLEncoder.encode(url, "utf-8");
            myTask.execute(new String[]{encoded});
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "Error while processing the Message.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    public class SendNFCTask extends AsyncTask<String, Void, String> {
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
        public SendNFCTask(Context context, List<NameValuePair> parameters) {
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
                this.dialog.setMessage("Processing NFC....");
            }
        }

        @Override
        protected void onPostExecute(final String success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }


            Intent intent = new Intent(getApplicationContext(), BookDetails.class);
            intent.putExtra(EXTRA_MESSAGE, success);
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
            HttpPost httppost = new HttpPost("http://libapp.byparamatma.com/appNFC.php");

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
