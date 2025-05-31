package com.example.apitest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteDataSource {
    private final Api1 api;

    public RemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apilayer.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api1.class);
    }

    public void validateNumber(String apiKey, String phoneNumber, Callback<PhoneValidationResponse> callback) {
        Call<PhoneValidationResponse> call = api.validateNumber(apiKey, phoneNumber);
        call.enqueue(callback);
    }
}
