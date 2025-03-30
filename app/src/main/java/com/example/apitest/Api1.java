package com.example.apitest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api1 {
    @GET("validate")
    Call<String> validateNumber(
            @Query("access_key") String accessKey,
            @Query("number") String phoneNumber
    );
}
