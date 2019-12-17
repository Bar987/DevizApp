package com.mobweb.devizapp.network;

import com.mobweb.devizapp.model.CurrencyData;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {
    private static final String SERVICE_URL = "https://api.exchangerate-api.com/v4/latest/";

    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    private Retrofit retrofit;
    private CurrencyApi currencyApi;

    private NetworkManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(SERVICE_URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        currencyApi = retrofit.create(CurrencyApi.class);
    }


    public Call<CurrencyData> getCurrency(String currency_code) {
        return currencyApi.getCurrency(currency_code);
    }
}

