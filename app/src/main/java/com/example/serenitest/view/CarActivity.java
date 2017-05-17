package com.example.serenitest.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.serenitest.R;
import com.example.serenitest.databinding.CarActivityBinding;
import com.example.serenitest.model.Car;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class CarActivity extends AppCompatActivity  {

    private CarActivityBinding carActivityBinding;

    // Entrer le lien du websocket
    private static final String WEBSOCKET_URL = "wss://........";
    private static final String CARS_REQUEST = "{\"Type\" : \"infos\"}";

    private ArrayList<Car> carList;
    OkHttpClient client;

    CarAdapter adapter;

    private Observable<String> requestObservable;
    private Observer<String> observer;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        carList = new ArrayList<>();
        client = new OkHttpClient();

        initDataBinding();
        setSupportActionBar(carActivityBinding.toolbar);
        setupListCarView(carActivityBinding.listCar);

        requestObservable = Observable.just(CARS_REQUEST);
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

//       L'action de cliquer sur le FloatingActionButton fait réagir l'observer en fonction de l'observable rxjava
        carActivityBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carActivityBinding.labelStatus.setVisibility(View.GONE);
                carActivityBinding.carProgress.setVisibility(View.VISIBLE);
                requestObservable.subscribe(observer);
            }
        });

    }

    private void initDataBinding() {
        carActivityBinding = DataBindingUtil.setContentView(this, R.layout.car_activity);
    }

    //  On fournit au recyclerview son adapter
    private void setupListCarView(RecyclerView listCar) {
        adapter = new CarAdapter(carList);
        listCar.setAdapter(adapter);
        listCar.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override protected void onDestroy() {
        super.onDestroy();
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
            JSONArray array = new JSONArray(txt);

            Gson gson = new Gson();
            for (int i=0;i<7;i++){
                JSONObject object = array.getJSONObject(i);
                Car car = gson.fromJson(object.toString(),Car.class);
                carList.add(car);
                adapter.notifyItemInserted(carList.size());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
                carActivityBinding.carProgress.setVisibility(View.GONE);
                carActivityBinding.listCar.setVisibility(View.VISIBLE);
            }
       });
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_car, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            start();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private final class CarWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send(CARS_REQUEST);
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
