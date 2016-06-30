package com.example.inquallity.beacons.presenter;

import android.support.annotation.NonNull;

import com.example.inquallity.beacons.view.SplashView;

/**
 * @author Maksim Radko
 */
public class SplashPresenter {

    @NonNull
    private final SplashView mView;

    public SplashPresenter(@NonNull SplashView view) {

        mView = view;
    }
}
