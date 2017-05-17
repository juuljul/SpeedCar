package com.example.serenitest.view;



import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.serenitest.R;
import com.example.serenitest.databinding.ItemCarBinding;
import com.example.serenitest.model.Car;
import com.example.serenitest.viewmodel.ItemCarViewModel;

import java.util.Collections;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarAdapterViewHolder> {

    private List<Car> carList;

    public CarAdapter() {
        this.carList = Collections.emptyList();
    }

    public CarAdapter(List<Car> carList) {
        this.carList = carList;
    }

    @Override public CarAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCarBinding itemCarBinding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_car,
                        parent, false);
        return new CarAdapterViewHolder(itemCarBinding);
    }

    @Override public void onBindViewHolder(CarAdapterViewHolder holder, int position) {
        holder.bindCar(carList.get(position));
    }

    @Override public int getItemCount() {
        return carList.size();
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
        notifyDataSetChanged();
    }

    public static class CarAdapterViewHolder extends RecyclerView.ViewHolder {
        ItemCarBinding mItemCarBinding;

        public CarAdapterViewHolder(ItemCarBinding itemCarBinding) {
            super(itemCarBinding.itemCar);
            this.mItemCarBinding = itemCarBinding;
        }

        void bindCar(Car car) {
            if (mItemCarBinding.getCarViewModel() == null) {
                mItemCarBinding.setCarViewModel(
                        new ItemCarViewModel(car, itemView.getContext()));
            } else {
                mItemCarBinding.getCarViewModel().setCar(car);
            }
        }
    }
}
