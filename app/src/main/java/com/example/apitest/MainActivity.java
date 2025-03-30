package com.example.apitest;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://apilayer.net/api/";
    private static final String API_KEY = "1cdb3ff7fefc93e774d11a7c7ab54063";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        Api1 api1 = retrofit.create(Api1.class);

        Call<String> call = api1.validateNumber(API_KEY, "+380669746594");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.i("ApiResponse", response.body());
                } else {
                    Log.e("ApiResponse", "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ApiResponse", "Error: " + t.getMessage());
            }
        });
    }
}
