package com.example.goertz.libraryapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class BookResultActivity extends Activity {


    private final String LOG_TAG = FeedbackActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_result);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_result, menu);


        Intent intent = getIntent();
        String message = intent.getStringExtra(BibScan.EXTRA_MESSAGE);
        if(message != null) {
            try {
                getJsonDate(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        return true;
    }

    private void getJsonDate(String json)
            throws JSONException {
        final String TITEL = "titel";
        final String ISBN = "isbn";
        final String LANGUAGE = "language";
        final String YEAR = "year";


        List valueList = new ArrayList<String>();

        JSONObject bookJson = new JSONObject(json);
        JSONArray jArr = bookJson.getJSONArray("book");

        for (int i=0; i < jArr.length()-1; i++) {

            JSONObject obj = jArr.getJSONObject(i);
            valueList.add(obj.getString(TITEL)+", "+
                            obj.getString(ISBN)+", "+
                            obj.getString(LANGUAGE)+", "+
                            obj.getString(YEAR)
            );

        }

        ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, valueList);
        final ListView lv = (ListView)findViewById(R.id.searchList);
        lv.setBackgroundResource(R.drawable.schwarz);

        //int r = getResources().getIdentifier("schwarz", "drawable", "com.example.goertz.libraryapp");
        //lv.setBackgroundResource(r);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

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
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Intent newActivity = new Intent(view.getContext(), BookDetails.class);

                newActivity.putExtra(BibScan.EXTRA_MESSAGE,jo.toString());
                //Log.e(LOG_TAG, text);
                startActivity(newActivity);

            }
        });


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
