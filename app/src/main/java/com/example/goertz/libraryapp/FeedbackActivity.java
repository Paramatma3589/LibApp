package com.example.goertz.libraryapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import java.util.List;


public class FeedbackActivity extends ActionBarActivity {

    private final String LOG_TAG = FeedbackActivity.class.getSimpleName();
    /** Contains the EditText for the email **/
    EditText emailEdit;
    /** Contains the EditText for the message **/
    EditText messageEdit;
    /** Contains the EditText for the name **/
    EditText nameEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Show the back button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the text fields containing the message and the name of the sender
        emailEdit = (EditText) findViewById(R.id.FeedbackEditEmail);
        messageEdit = (EditText) findViewById(R.id.FeedbackEditNachricht);
        nameEdit = (EditText) findViewById(R.id.BibEditSearch);
    }

    public void sendFeedback(View view) {
        String email = emailEdit.getText().toString();
        String message = messageEdit.getText().toString();
        String name = nameEdit.getText().toString();

        // Check if both fields are filled, when not show an error
        if (message.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter a Text!", Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "Text Missed ");
        } else {
            // Send the feedback to the given url
            String url = "http://libapp.byparamatma.com/appfeedback.php";

            // Build get parameters
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
            nameValuePair.add(new BasicNameValuePair("recipient", "app"));
            nameValuePair.add(new BasicNameValuePair("email", email));
            nameValuePair.add(new BasicNameValuePair("message", message));
            nameValuePair.add(new BasicNameValuePair("name", name));

            SendFeedbackTask myTask = new SendFeedbackTask(this, nameValuePair);

            // URL must be encoded because it can't contain white spaces etc.
            try {
                String encoded = URLEncoder.encode(url, "utf-8");
                myTask.execute(new String[] {encoded});
            } catch (UnsupportedEncodingException e) {
                Log.d(LOG_TAG, "Error while processing the Message."+e.getMessage());
                Toast.makeText(this, "Error while processing the Message.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SendFeedbackTask extends AsyncTask<String, Void, Boolean> {
        /** progress dialog to show user that the sending is processing. */
        private ProgressDialog dialog;
        /** The context of this task **/
        private Context context;
        /** Parameters for the URL **/
        private List<NameValuePair> parameters;

        /**
         * Constructor
         *
         * Initializes the progress dialog and sets the context and the parameters.
         */
        public SendFeedbackTask(Context context, List<NameValuePair> parameters) {
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
                this.dialog.setMessage("Processing Feedback....");
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (success) {
                Toast.makeText(context, "Feedback is send, Thank you!", Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                Toast.makeText(context, "Error while sending, please send again...", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Error while sending");
            }
        }


        protected Boolean doInBackground(final String... urls) {
            if (isOnline()) {
                String response = executeHttpGet(urls[0]);

                if (response != null && response.equals("1")) {
                    return true;
                }
            }

            return false;
        }


        private String executeHttpGet(String url) {
            String response = null;

            // Initialize HttpClient and HttpPost
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://libapp.byparamatma.com/appfeedback.php");

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
                Log.d(LOG_TAG, "ONLINE");
                return true;
            }
            Log.d(LOG_TAG, "OFFLINE");
            return false;

        }
    }
}
