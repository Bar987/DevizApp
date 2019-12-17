package com.mobweb.devizapp.network;

import com.mobweb.devizapp.model.CurrencyData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CurrencyApi {
    @GET("{currency_code}")
    Call<CurrencyData> getCurrency(@Path("currency_code") String currency_code);
}
