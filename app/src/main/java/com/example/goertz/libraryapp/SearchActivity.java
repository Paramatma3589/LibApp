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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "MESSAGE";
    private final String LOG_TAG = SearchActivity.class.getSimpleName();
    /** Contains the EditText for the name **/
    EditText searchEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("Library Search");

        // Show the back button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the text fields containing the message and the name of the sender
        searchEdit = (EditText) findViewById(R.id.BibEditSearch);

    }

    public void sendSearch(View view) {
        String search = searchEdit.getText().toString();

        // Check if both fields are filled, when not show an error
        if (search.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter a Text!", Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "Text Missed ");
        } else {
            // Send the feedback to the given url
            String url = "http://libapp.byparamatma.com/appsearch.php";

            // Build get parameters
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
            nameValuePair.add(new BasicNameValuePair("recipient", "app"));
            nameValuePair.add(new BasicNameValuePair("search", search));
            SendSearchTask myTask = new SendSearchTask(this, nameValuePair);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
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


    public class SendSearchTask extends AsyncTask<String, Void, String> {
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
        public SendSearchTask(Context context, List<NameValuePair> parameters) {
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
                this.dialog.setMessage("Processing Search....");
            }
        }


        @Override
        protected void onPostExecute(final String success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            String json = success;
            //Intent intent = new Intent(getApplicationContext(), BookResultActivity.class);
            //intent.putExtra(EXTRA_MESSAGE, success);
            //startActivity(intent);
            if (json != null) {
                try {
                    final String TITEL = "titel";
                    final String ISBN = "isbn";
                    final String LANGUAGE = "language";
                    final String YEAR = "year";
                    final String AUTHOR1 = "firstname";
                    final String AUTHOR = "lastname";
                    final String SHELF = "shelfName";
                    final String DESC = "description";


                    List valueList = new ArrayList<String>();

                    JSONObject bookJson = new JSONObject(json);
                    JSONArray jArr = bookJson.getJSONArray("book");

                    for (int i = 0; i < jArr.length() - 1; i++) {

                        JSONObject obj = jArr.getJSONObject(i);
                        valueList.add(obj.getString(TITEL) + ", " +
                                        obj.getString(ISBN) + ", " +
                                        obj.getString(LANGUAGE) + ", " +
                                        obj.getString(YEAR) + ", " +
                                        obj.getString(AUTHOR1) + ", " +
                                        obj.getString(AUTHOR) + ", " +
                                        obj.getString(SHELF) + ", " +
                                        obj.getString(DESC)

                        );
                    }
                    ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, valueList);
                    final ListView lv = (ListView)findViewById(R.id.searchList);


                    //int r = getResources().getIdentifier("schwarz", "drawable", "com.example.goertz.libraryapp");
                    //lv.setBackgroundResource(r);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            String info = ((TextView) view).getText().toString();
                            //Toast.makeText(BookResultActivity.this, "" + position + info, Toast.LENGTH_SHORT).show();

                            String[] result = info.split(",");
                            JSONObject jo = new JSONObject();


                            try {
                                jo.put("titel", result[0]);
                                jo.put("isbn", result[1]);
                                jo.put("language", result[2]);
                                jo.put("year", result[3]);
                                jo.put("firstname", result[4]);
                                jo.put("lastname", result[5]);
                                jo.put("shelfName", result[6]);
                                jo.put("description", result[7]);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            Intent newActivity = new Intent(view.getContext(), BookDetails.class);

                            newActivity.putExtra(BibScan.EXTRA_MESSAGE,jo.toString());
                            //Log.e(LOG_TAG, text);
                            startActivity(newActivity);

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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
            HttpPost httppost = new HttpPost("http://libapp.byparamatma.com/appsearch.php");

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
