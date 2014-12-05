package com.example.goertz.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class BookDetails extends ActionBarActivity {

    private final String LOG_TAG = FeedbackActivity.class.getSimpleName();
    TextView titleText;
    TextView authorText;
    TextView isbnText;
    TextView publishedText;
    TextView languageText;
    TextView yearText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        titleText = (TextView) findViewById(R.id.titleText);
        authorText = (TextView) findViewById(R.id.authorText);
        isbnText = (TextView) findViewById(R.id.isbnText);
        publishedText = (TextView) findViewById(R.id.publishedText);
        languageText = (TextView) findViewById(R.id.languageText);
        yearText = (TextView) findViewById(R.id.yearText);
        Intent intent = getIntent();
        String message = intent.getStringExtra(BibScan.EXTRA_MESSAGE);
        if(message != null) {
            try {
                 getJsonDate(message);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
        }


    }
    private void getJsonDate(String json)
        throws JSONException {
            final String TITEL = "titel";
            final String ISBN = "isbn";
            final String LANGUAGE = "language";
            final String YEAR = "year";

        JSONObject bookJson = new JSONObject(json);

        titleText.setText(bookJson.getString(TITEL));
        isbnText.setText(bookJson.getString(ISBN));
        languageText.setText(bookJson.getString(LANGUAGE));
        yearText.setText(bookJson.getString(YEAR));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.book_details, menu);
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
}
