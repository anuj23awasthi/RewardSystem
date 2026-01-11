package com.example.sharprewards.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.1.102:5000/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {

            // 🔥 Enable full HTTP logs
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)   // <-- USE LOGGING CLIENT
                    .build();
        }
        return retrofit;
    }
}
