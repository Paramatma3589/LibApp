package com.example.goertz.libraryapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class InfoBookActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_book);
        setTitle("Library Information");
        TextView txtcontact = (TextView)findViewById(R.id.bibContact);
        TextView txtopen = (TextView)findViewById(R.id.bibOpeningTime);
        txtopen.setText(Html.fromHtml("<h1>Opening hours:</h1>"+
                "<b>Monday:</b><br>1:00 pm – 7:00 pm<br>"+
        "<b>Tuesday/Wednesday/Friday</b><br>09:30 am – 7:00 pm<br>"+
        "<b>Thursday:</b><br>" +
                "09:30 am – 12:00 am <br>" +
                "2:00 pm – 7:00 pm<br>"+

                ""));
        txtcontact.setText(Html.fromHtml("<h1>Contact</h1>" +
                "<b>Room:</b> &nbsp;A4.09<br>" +
                "<b>Phone:</b> &nbsp;+43 1 333 40 77 DW 207 - 211<br>" +
                "<b>Email: </b>&nbsp;bibliothek@technikum-wien.at<br>"+
                ""));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info_book, menu);
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
