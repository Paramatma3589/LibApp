package com.example.goertz.libraryapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;


public class MapActivity extends ActionBarActivity {
    EditText searchEdit;
    drawFH fhview;
    TextView person;
    TextView room;
    TextView floor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setTitle("Technikum Wien Map");
        fhview = new drawFH(this);
        FrameLayout a = (FrameLayout) findViewById(R.id.viewMapFH);
        a.addView(fhview);
        searchEdit = (EditText) findViewById(R.id.SearchRoom);
        person = (TextView) findViewById(R.id.PeopleText);
        room = (TextView) findViewById(R.id.roomText);
        floor = (TextView) findViewById(R.id.floorText);


        searchEdit.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // Get the input of the search field and load new results
                String search = searchEdit.getText().toString();
                fhview.setSearchroom(search);

                fhview.invalidate();
                room.setText(fhview.getRoom());
                floor.setText(fhview.getFloor());


            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
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
