package com.example.serenitest.viewmodel;



import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.example.serenitest.R;
import com.example.serenitest.model.Car;
import com.example.serenitest.view.CarDetailActivity;


public class ItemCarViewModel extends BaseObservable {

    private Car car;
    private Context context;

    public ItemCarViewModel(Car car, Context context) {
        this.car = car;
        this.context = context;
    }

    public String getCarName() {
        return car.getName();
    }

    public String getBrandName() {
        return car.getBrand();
    }

    public String getSpeedMax() {
        String speedMax = String.valueOf(car.getSpeedMax());
        return "Vitesse maximale : " +speedMax + " km/h";
    }

//   Suivant la marque, on attribue la photo correspondante
    public Drawable getImageRes() {

        switch (car.getBrand()){
            case "Aston Martin":
                return context.getResources().getDrawable(R.drawable.mini);
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

     public void onItemClick(View view) {
         context.startActivity(CarDetailActivity.launchDetail(view.getContext(), car));
     }

    public void setCar(Car car) {
        this.car = car;
        notifyChange();
    }
}
