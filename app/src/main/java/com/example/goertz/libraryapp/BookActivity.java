package com.example.goertz.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class BookActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book);
        ImageButton btninfo = (ImageButton)findViewById(R.id.btnInfo);
        btninfo.setOnClickListener(this);
        ImageButton btnSearch = (ImageButton)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        ImageButton btnbookmap = (ImageButton)findViewById(R.id.btnBookMap);
        btnbookmap.setOnClickListener(this);
        ImageButton btnnfc = (ImageButton)findViewById(R.id.btnNFC);
        btnnfc.setOnClickListener(this);
        ImageButton btnBarCode = (ImageButton)findViewById(R.id.btnBarcode);
        btnBarCode.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.book, menu);
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

    private void showInfo(){

        startActivity( new Intent(this,InfoBookActivity.class));
        startActivity( new Intent(this,InfoBookActivity.class));
    }
    private void btnSearch(){

        startActivity( new Intent(this,SearchActivity.class));
    }
    private void btnbookmap(){
        startActivity( new Intent(this,BookMapActivity.class));
    }
    private void btnNfc(){
        startActivity( new Intent(this,BibScan.class));
    }

    private void btnBarcode(){
        startActivity( new Intent(this,BarCodeScan.class));
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnInfo:
                showInfo();
                break;
            case R.id.btnSearch:
                btnSearch();
                break;
            case R.id.btnBookMap:
                btnbookmap();
                break;
            case R.id.btnNFC:
                btnNfc();
                break;
            case R.id.btnBarcode:
                btnBarcode();
                break;
        }

    }
}
