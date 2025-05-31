package com.example.apitest;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executors;

public class LocalDataSource {
    private final DBDao dao;

    public LocalDataSource(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        dao = db.dao();
    }

    public void insertEntry(DB entry) {
        Executors.newSingleThreadExecutor().execute(() -> dao.insert(entry));
    }

    public LiveData<List<DB>> getAllEntries() {
        return dao.getAllLiveData();
    }
}
