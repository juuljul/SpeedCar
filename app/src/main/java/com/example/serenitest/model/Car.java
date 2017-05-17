package com.example.serenitest.model;


import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class Car implements Serializable{

    String Brand;
    String Name;
    int SpeedMax;
    int Cv;
    double CurrentSpeed;


    public Car(String brand, String name, int speedMax, int cv, double currentSpeed) {
        Brand = brand;
        Name = name;
        SpeedMax = speedMax;
        Cv = cv;
        CurrentSpeed = currentSpeed;

    }

    public String getBrand() {
        return Brand;
    }

    public String getName() {
        return Name;
    }

    public int getSpeedMax() {
        return SpeedMax;
    }

    public int getCv() {
        return Cv;
    }

    public double getCurrentSpeed() {
        return CurrentSpeed;
    }

}
