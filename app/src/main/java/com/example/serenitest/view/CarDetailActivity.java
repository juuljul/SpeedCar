package com.example.serenitest.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.serenitest.R;
import com.example.serenitest.databinding.CarDetailActivityBinding;
import com.example.serenitest.model.Car;
import com.example.serenitest.viewmodel.CarDetailViewModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;


public class CarDetailActivity extends AppCompatActivity {

    // Entrer le lien du websocket
    private static final String WEBSOCKET_URL = "wss://.......";
    private static final String EXTRA_CAR = "EXTRA_CAR";
    private static final String START_CAR_REQUEST = "{\"Type\" : \"start\", \"Payload\" : {\"Name\" : \"";
    private static final String END_CAR_REQUEST = "\"}}";

    private CarDetailActivityBinding carDetailActivityBinding;

    private OkHttpClient client;

    private String carRequest;
    private Car car, currentCar;

    private Observable<String> requestObservable;
    private Observer<String> observer;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = new OkHttpClient();

        carDetailActivityBinding =
                DataBindingUtil.setContentView(this, R.layout.car_detail_activity);
        setSupportActionBar(carDetailActivityBinding.toolbar);
        displayHomeAsUpEnabled();
        getExtrasFromIntent();

        carRequest = START_CAR_REQUEST + car.getName() + END_CAR_REQUEST;

        requestObservable = Observable.just(carRequest);

        observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(String value) {
                start();
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };

        carDetailActivityBinding.fabStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carDetailActivityBinding.vitesseCard.setVisibility(View.VISIBLE);
                requestObservable.subscribe(observer);
            }
        });

    }

    public static Intent launchDetail(Context context, Car car) {
        Intent intent = new Intent(context, CarDetailActivity.class);
        intent.putExtra(EXTRA_CAR, car);
        return intent;
    }

    private void displayHomeAsUpEnabled() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getExtrasFromIntent() {
        car = (Car) getIntent().getSerializableExtra(EXTRA_CAR);
        CarDetailViewModel carDetailViewModel = new CarDetailViewModel(car, this);
        carDetailActivityBinding.setCarDetailViewModel(carDetailViewModel);
    }

    // Connection à la websocket
    public void start() {
        Request request = new Request.Builder().url(WEBSOCKET_URL).build();
        CarWebSocketListener listener = new CarWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    public void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject(txt);
                    Gson gson = new Gson();
                    currentCar = gson.fromJson(object.toString(),Car.class);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                carDetailActivityBinding.carProgress.setVisibility(View.GONE);
                carDetailActivityBinding.currentSpeed.setText(String.valueOf(currentCar.getCurrentSpeed()));
            }
        });
    }


    private final class CarWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send(carRequest);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output(text);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        }
    }

}
