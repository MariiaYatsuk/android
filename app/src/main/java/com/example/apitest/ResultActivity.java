package com.example.apitest;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://apilayer.net/api/";
    private static final String API_KEY = "1cdb3ff7fefc93e774d11a7c7ab54063";

    private TextView textViewResult;

    // Room
    private AppDatabase appDatabase;
    private DBDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textViewResult = findViewById(R.id.textViewResult);

        // Ініціалізуємо Room через синглтон
        appDatabase = AppDatabase.getInstance(this);
        dao = appDatabase.dao();

        String phoneNumber = getIntent().getStringExtra("phone_number");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api1 api = retrofit.create(Api1.class);
        Call<PhoneValidationResponse> call = api.validateNumber(API_KEY, phoneNumber);

        call.enqueue(new Callback<PhoneValidationResponse>() {
            @Override
            public void onResponse(Call<PhoneValidationResponse> call,
                                   @NonNull Response<PhoneValidationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PhoneValidationResponse data = response.body();

                    // Підготовка рядка для UI
                    String result = "Номер: " + data.number + "\n" +
                            "Країна: " + data.countryName + "\n" +
                            "Оператор: " + data.carrier + "\n" +
                            "Тип лінії: " + data.lineType + "\n" +
                            "Місто: " + (data.location != null ? data.location : "Немає даних") + "\n" +
                            "Дійсний: " + (data.valid ? "Так" : "Ні");

                    // Вставка в БД у фоні
                    Executors.newSingleThreadExecutor().execute(() -> {
                        DB entry = new DB();
                        entry.number      = data.number;
                        entry.countryName = data.countryName;
                        entry.carrier     = data.carrier;
                        entry.lineType    = data.lineType;
                        entry.location    = data.location;
                        entry.valid       = data.valid;
                        dao.insert(entry);
                    });

                    // Оновлення UI
                    runOnUiThread(() -> textViewResult.setText(result));

                } else {
                    runOnUiThread(() -> textViewResult.setText("Помилка: " + response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PhoneValidationResponse> call, Throwable t) {
                runOnUiThread(() -> textViewResult.setText("Помилка запиту: " + t.getMessage()));
            }
        });
    }
}
