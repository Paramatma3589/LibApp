package com.example.goertz.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class MainActivity extends ActionBarActivity{
    private static final String LOG_TAG = MapActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Technikum Wien App");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener{

        View rootView;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ImageButton btnbook = (ImageButton)rootView.findViewById(R.id.btnbook);
            btnbook.setOnClickListener(this);
            ImageButton btnContact = (ImageButton)rootView.findViewById(R.id.btnContact);
            btnContact.setOnClickListener(this);
            ImageButton btnMensa = (ImageButton)rootView.findViewById(R.id.btnmensa);
            btnMensa.setOnClickListener(this);
            ImageButton btnKalender = (ImageButton)rootView.findViewById(R.id.btnkalender);
            btnKalender.setOnClickListener(this);
            ImageButton btnNews = (ImageButton)rootView.findViewById(R.id.btnNews);
            btnNews.setOnClickListener(this);
            ImageButton btnFeedback = (ImageButton)rootView.findViewById(R.id.btnfeedback);
            btnFeedback.setOnClickListener(this);
            ImageButton btnMap = (ImageButton)rootView.findViewById(R.id.btnmap);
            btnMap.setOnClickListener(this);

            return rootView;
        }

        private void btnBook(){
            startActivity( new Intent(super.getActivity(),BookActivity.class));
        }

        private void btnMap(){
            startActivity( new Intent(super.getActivity(),MapActivity.class));
        }

        private void btnNews(){
            Intent intent_News = new Intent(super.getActivity(),KalenderActivity.class);
            intent_News.putExtra("title", "News");
            intent_News.putExtra("url", "http://www.technikum-wien.at/fh/news/");
            startActivity(intent_News);
        }

        private void btnContact(){
            startActivity( new Intent(super.getActivity(),ContactActivity.class));
        }

        private void btnMensa(){
            Intent intent_News = new Intent(super.getActivity(),KalenderActivity.class);
            intent_News.putExtra("title", "Mensa");
            Calendar calendar = new GregorianCalendar();
            Date trialTime = new Date();
            calendar.setTime(trialTime);
            Log.v(LOG_TAG,"Kalenderwoche:" +
                    calendar.get(Calendar.WEEK_OF_YEAR));
            intent_News.putExtra("url", "http://www.miaandmason.com/start/images/Speiseplan%20FH%20Technikum%20KW%20"+calendar.get(Calendar.WEEK_OF_YEAR)+".JPG");
            startActivity(intent_News);
        }

        private void btnKalender(){
            Intent intent_Kalender = new Intent(super.getActivity(),KalenderActivity.class);
            intent_Kalender.putExtra("title", "Schedule Plan");



// You'll need to handle the exceptions thrown by execute()



            //intent_Kalender.putExtra("url", "https://cis.technikum-wien.at/cis/private/lvplan/stpl_week.php?pers_uid=wi14m056");
            intent_Kalender.putExtra("url", "http://libapp.byparamatma.com/cis.html");

//            intent_Kalender.putExtra("url", "https://cis.technikum-wien.at/cis/private/lvplan/stpl_week.php?pers_uid=wi14m056");
            startActivity(intent_Kalender);

            //startActivity( new Intent(super.getActivity(),KalenderActivity.class));
        }
        private void btnFeedback(){
            startActivity( new Intent(super.getActivity(),FeedbackActivity.class));
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnbook:
                    btnBook();
                    break;
                case R.id.btnContact:
                    btnContact();
                    break;
                case R.id.btnmensa:
                    btnMensa();
                    break;
                case R.id.btnkalender:
                    btnKalender();
                    break;
                case R.id.btnNews:
                    btnNews();
                    break;
                case R.id.btnfeedback:
                    btnFeedback();
                    break;
                case R.id.btnmap:
                    btnMap();
                    break;
            }

        }

    }
}
