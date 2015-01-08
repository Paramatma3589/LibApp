package com.example.goertz.libraryapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class drawShelf extends View {

    Paint paint = new Paint();

    public drawShelf(Context context) {

        super(context);

    }
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect = new RectF();
        //initial Color and Mode : Blue Color + not filled
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        //get Elements and draw in Canvas
        String json;
        StringBuilder finalString = new StringBuilder();
        InputStream isStream = getResources().openRawResource(R.raw.liblayout);
        BufferedReader bReader = new BufferedReader(new InputStreamReader(isStream));
        String nextShelf = null;

        float width = 40; // breite in Meter
        float height = 60; //länge/tiefe in Meter
        float width_factor = canvas.getWidth() / width;     //faktor 1m physisch --> x Einheiten im Canvas
        float height_factor = canvas.getHeight() / height;  //faktor 1m physisch --> x Einheiten im Canvas
        int bookID;
        try {
             bookID = Integer.parseInt(BookMapActivity.bookID);
        }catch (Exception e){
            bookID= 0;
        }

        try {
            while ((json = bReader.readLine()) != null) {
                finalString.append(json);
                nextShelf = json.toString();            //get next line/shelf
                String[] parts = nextShelf.split(","); //Separator
                int id = Integer.parseInt(parts[0]);   //Element 1
                float x = Float.parseFloat(parts[1]);  //Element 2
                float y = Float.parseFloat(parts[2]);  //Element 3
                float w = Float.parseFloat(parts[3]);  //Element 4
                float d = Float.parseFloat(parts[4]);  //Element 5
                //Log.e("","wight: "+width_factor+"high: "+height_factor);
                rect.set(x * width_factor, y * height_factor, w * width_factor, d * height_factor);
                //Shelf where the book is placed
                if (id == bookID) {
                    paint.setColor(Color.RED);
                    paint.setStyle(Paint.Style.FILL);

                //PC Terminal
                } else if (id == 9999) {
                    paint.setColor(Color.CYAN);
                    paint.setStyle(Paint.Style.FILL);

                //Office
                } else if (id == 8888) {
                    //Office
                    paint.setColor(Color.GREEN);
                    paint.setStyle(Paint.Style.FILL);

                //Entrance
                } else if (id == 7777) {
                    paint.setColor(Color.YELLOW);
                    paint.setStyle(Paint.Style.FILL);

                //Normal Shelf
                } else {
                    paint.setColor(Color.BLUE);
                    paint.setStyle(Paint.Style.FILL);

                }
                //draw shelf with coordinates + filled with color given above
                canvas.drawRect(rect, paint);
            }

            isStream.close();
            bReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
