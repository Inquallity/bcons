package com.example.inquallity.beacons.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Maksim Radko
 */
public class Provider {

    private static String sToken;

    public static Retrofit provide() {
        return new Retrofit.Builder()
                .baseUrl("https://proximitybeacon.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(provideClient())
                .build();
    }

    private static OkHttpClient provideClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final Request request = chain.request();
                        final Request newRequest = request.newBuilder().addHeader("Authorization", "Bearer " + sToken).build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();
    }

    public static void refreshKey(String data) {
        sToken = data;
    }
}
