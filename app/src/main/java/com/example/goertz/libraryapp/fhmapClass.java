package com.example.goertz.libraryapp;

public class fhmapClass {

    String RaumName;
    String floor;
    String name;
    String Raumnummer;
    float x;
    float y;
    float w;
    float d;
    int personen;

    public String getRaumName() {
        return RaumName;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getX() {
        return x;
    }

    public String getRaumnummer() {

        return Raumnummer;
    }

    public void setRaumnummer(String raumnummer) {
        Raumnummer = raumnummer;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float getD() {
        return d;
    }

    public void setD(float d) {
        this.d = d;
    }

    public int getPersonen() {
        return personen;
    }

    public void setPersonen(int personen) {
        this.personen = personen;
    }

    public void setRaumName(String raumName) {
        RaumName = raumName;

    }

    public fhmapClass(String RaumName, String floor, String name, String Raumnummer, float x, float y, float w, float d, int personen){

        this.RaumName = RaumName;
        this.floor = floor;
        this.name = name;

        this.Raumnummer = Raumnummer;
        this.x = x;
        this.y= y;
        this.w = w;
        this.d= d;
        this.personen = personen;
    }
}
