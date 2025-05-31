package com.example.apitest;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

public class ResultActivity extends AppCompatActivity {

    private static final String API_KEY = "1cdb3ff7fefc93e774d11a7c7ab54063";

    private TextView textViewResult;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textViewResult = findViewById(R.id.textViewResult);
        repository = Repository.getInstance(this);

        String phoneNumber = getIntent().getStringExtra("phone_number");


        repository.getPhoneLiveData().observe(this, new Observer<PhoneValidationResponse>() {
            @Override
            public void onChanged(PhoneValidationResponse data) {
                if (data != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Номер: ").append(data.number).append("\n");
                    sb.append("Країна: ").append(data.countryName).append("\n");
                    sb.append("Оператор: ").append(data.carrier).append("\n");
                    sb.append("Тип лінії: ").append(data.lineType).append("\n");

                    if (!"mobile".equalsIgnoreCase(data.lineType)) {
                        // Якщо location == null або порожнє, можна виводити "Немає даних"
                        String city = (data.location != null && !data.location.isEmpty())
                                ? data.location
                                : "Немає даних";
                        sb.append("Місто: ").append(city).append("\n");
                    }

                    sb.append("Дійсний: ").append(data.valid ? "Так" : "Ні");

                    textViewResult.setText(sb.toString());
                }
            }
        });

        repository.validateAndSave(API_KEY, phoneNumber);
    }
}
