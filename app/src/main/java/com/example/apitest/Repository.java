package com.example.apitest;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static Repository instance;
    private final LocalDataSource localDataSource;
    private final RemoteDataSource remoteDataSource;
    // LiveData, у яке ми будемо “постити” результат API
    private final MutableLiveData<PhoneValidationResponse> phoneLiveData = new MutableLiveData<>();

    private Repository(Context context) {
        localDataSource = new LocalDataSource(context);
        remoteDataSource = new RemoteDataSource();
    }

    public static Repository getInstance(Context context) {
        if (instance == null) {
            synchronized (Repository.class) {
                if (instance == null) {
                    instance = new Repository(context);
                }
            }
        }
        return instance;
    }

    public LiveData<PhoneValidationResponse> getPhoneLiveData() {
        return phoneLiveData;
    }

    public void validateAndSave(String apiKey, String phoneNumber) {
        remoteDataSource.validateNumber(apiKey, phoneNumber, new Callback<PhoneValidationResponse>() {
            @Override
            public void onResponse(Call<PhoneValidationResponse> call, Response<PhoneValidationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PhoneValidationResponse data = response.body();

                    phoneLiveData.postValue(data);

                    DB entry = new DB();
                    entry.number = data.number;
                    entry.countryName = data.countryName;
                    entry.carrier = data.carrier;
                    entry.lineType = data.lineType;
                    entry.location = data.location;
                    entry.valid = data.valid;

                    localDataSource.insertEntry(entry);
                }
            }

            @Override
            public void onFailure(Call<PhoneValidationResponse> call, Throwable t) {
                Log.e(TAG, "Network failure while validating number", t);
                phoneLiveData.postValue(null);
            }
        });
    }

    // Додатковий метод: якщо десь захочеться забрати всі записи з БД у вигляді LiveData
    public LiveData<List<DB>> getAllEntries() {
        return localDataSource.getAllEntries();
    }
}
