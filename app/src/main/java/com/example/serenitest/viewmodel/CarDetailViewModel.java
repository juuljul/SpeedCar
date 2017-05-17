package com.example.serenitest.viewmodel;


import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.serenitest.R;
import com.example.serenitest.model.Car;

import java.util.Observable;

public class CarDetailViewModel extends Observable {

    private Car car;
    private Context context;

    public CarDetailViewModel(Car car, Context context) {
        this.car = car;
        this.context = context;
    }

    public String getCarName() {
         return car.getName();
    }

    public String getSpeedMax() {
        String speedMax = String.valueOf(car.getSpeedMax());
        return "Vitesse max : " +speedMax + " km/h";

    }

    //   Suivant la marque, on attribue la photo correspondante
    public Drawable getImageRes() {
        switch (car.getBrand()){
            case "Aston Martin":
                return context.getResources().getDrawable(R.drawable.mini_large);
            case "Peugeot":
                return context.getResources().getDrawable(R.drawable.peugeot);
            case "Porsche":
                return context.getResources().getDrawable(R.drawable.porsche);
            case "Ferrari":
                return context.getResources().getDrawable(R.drawable.ferrari);
            case "BMW":
                return context.getResources().getDrawable(R.drawable.bmw);
            case "Jaguar":
                return context.getResources().getDrawable(R.drawable.jaguar);
            case "Audi":
                return context.getResources().getDrawable(R.drawable.audi);
            default:
                return context.getResources().getDrawable(R.drawable.default_car);
        }
    }

    public String getBrandName() {
        return car.getBrand();
    }


    public String getChevaux() {

        return "Puissance max : " + String.valueOf(car.getCv()) + " chevaux";
    }


}
