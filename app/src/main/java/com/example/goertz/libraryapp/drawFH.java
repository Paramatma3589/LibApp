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
import java.util.ArrayList;

public class drawFH extends View {

    Paint paint = new Paint();
    String searchroom;
    String person= "";
    String floor= "";
    String room= "";

    public String getPerson() {
        return person;
    }

    public String getFloor() {
        return floor;
    }

    public String getRoom() {
        return room;
    }

    public void setSearchroom(String searchroom) {
        this.searchroom = searchroom;
    }

    public drawFH(Context contextFH) {
        super(contextFH);
    }

    @Override
    public void onDraw(Canvas canvas) {


        float width_factor =0;
        float height_factor=0;


        super.onDraw(canvas);
        RectF rect = new RectF();

        //initial Color and Mode : Blue Color + not filled
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);



        //get Elements and draw in Canvas
        String json;
        StringBuilder finalString = new StringBuilder();
        InputStream isStream = getResources().openRawResource(R.raw.fhmap);
        BufferedReader bReader = new BufferedReader(new InputStreamReader(isStream));
        String nextShelf = null;

        float width = 0;
        float height = 0;

        int i=0;
        String search = searchroom;
        String floorSearch ="";



        ArrayList<fhmapClass> fhmap = new ArrayList <fhmapClass>();
        try {
            while ((json = bReader.readLine()) != null) {
                finalString.append(json);

                nextShelf = json.toString();            //get next line/shelf

                String[] parts = nextShelf.split(","); //Separator
                if (parts[0].equals(search)) {
                    floorSearch = parts[1];
                    room = parts[2];
                    System.out.println("ANZEIGEN: " +parts[1]);
                    floor = parts[1];
                    person = parts[8];
                }
                fhmapClass f1 = new fhmapClass(parts[0], parts[1], parts[2], parts[3], Float.parseFloat(parts[4]),
                        Float.parseFloat(parts[5]), Float.parseFloat(parts[6]), Float.parseFloat(parts[7]), Integer.parseInt(parts[8]));
                fhmap.add(f1);

                i++;
            }
            for (int j = 0; j < fhmap.size(); j++) {

                if(floorSearch.equals("")|| search.equals("0")){
                    if(fhmap.get(j).getRaumName().equals("FH")) {
                        //factor FH
                        width = 130; // breite in Meter
                        height = 265; //länge/tiefe in Meter
                        width_factor = canvas.getWidth() / width;     //faktor 1m physisch --> x Einheiten im Canvas
                        height_factor = canvas.getHeight() / height;  //faktor 1m physisch --> x Einheiten im Canvas

                        float x = fhmap.get(j).getX();
                        float y = fhmap.get(j).getY();
                        float w = fhmap.get(j).getW();
                        float d = fhmap.get(j).getD();

                        if (fhmap.get(j).getFloor().equals("Haus_A")) {
                            paint.setColor(Color.BLUE);
                            paint.setStyle(Paint.Style.FILL);


                            //PC Terminal
                        } else if (fhmap.get(j).getFloor().equals("Haus_B")) {
                            paint.setColor(Color.CYAN);
                            paint.setStyle(Paint.Style.FILL);

                            //Office
                        } else if (fhmap.get(j).getFloor().equals("Haus_F")) {
                            //Office
                            paint.setColor(Color.GREEN);
                            paint.setStyle(Paint.Style.FILL);

                            //Entrance
                        } else if (fhmap.get(j).getFloor().equals("Bruecke")) {
                            paint.setColor(Color.GRAY);
                            paint.setStyle(Paint.Style.FILL);
                        }
                        rect.set(x * width_factor, y * height_factor, w * width_factor, d * height_factor);
                        canvas.drawRect(rect, paint);
                        paint.setColor(Color.RED);
                        paint.setTextSize(20);
                        if(!fhmap.get(j).getFloor().equals("Bruecke"))
                        {

                            canvas.drawText(fhmap.get(j).getFloor(), ((w - x) / 2 + x) * width_factor, ((d - y) / 2 + y) * height_factor, paint);
                        }



                    }
                }
                else if (fhmap.get(j).getFloor().equals(floorSearch)) {
                    width = 55; // breite in Meter
                    height = 100;
                    width_factor = canvas.getWidth() / width;     //faktor 1m physisch --> x Einheiten im Canvas
                    height_factor = canvas.getHeight() / height;  //faktor 1m physisch --> x Einheiten im Canvas

                    float x = fhmap.get(j).getX();
                    float y = fhmap.get(j).getY();
                    float w = fhmap.get(j).getW();
                    float d = fhmap.get(j).getD();
                    //person = fhmap.get(j).getPersonen();

                    rect.set(x * width_factor, y * height_factor, w * width_factor, d * height_factor);
                    if (!fhmap.get(j).getName().equals("Stockwerk")) {
                        if(fhmap.get(j).getRaumName().equals(search)){
                            paint.setTextSize(20);
                            paint.setColor(Color.RED);
                            paint.setStyle(Paint.Style.FILL);
                            canvas.drawRect(rect, paint);
                            paint.setColor(Color.BLACK);

                            canvas.drawText(fhmap.get(j).getRaumName(), ((w - x) / 2 + x) * width_factor, ((d - y) / 2 + y) * height_factor, paint);
                        }else {
                            paint.setTextSize(20);
                            paint.setColor(Color.BLACK);
                            paint.setStyle(Paint.Style.STROKE);
                            canvas.drawText(fhmap.get(j).getRaumName(), ((w - x) / 2 + x) * width_factor, ((d - y) / 2 + y) * height_factor, paint);
                            canvas.drawRect(rect, paint);

                        }
                    }
                    else{
                        paint.setColor(Color.BLACK);
                        paint.setStyle(Paint.Style.STROKE);
                        canvas.drawRect(rect, paint);
                    }
                }
            }
            isStream.close();
            bReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}