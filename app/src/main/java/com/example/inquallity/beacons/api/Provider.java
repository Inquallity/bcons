package com.example.inquallity.beacons.api;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Maksim Radko
 */
public class Provider {

    public static Retrofit provide(@NonNull String token) {
        return new Retrofit.Builder()
                .baseUrl("https://proximitybeacon.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(provideClient(token))
                .build();
    }

    private static OkHttpClient provideClient(final String token) {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final Request request = chain.request();
                        final Request newRequest = request.newBuilder().addHeader("Authorization", "Bearer " + token).build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();
    }
}
