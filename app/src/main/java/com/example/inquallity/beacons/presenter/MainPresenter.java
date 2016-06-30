package com.example.inquallity.beacons.presenter;

import android.support.annotation.NonNull;

import com.example.inquallity.beacons.view.MainView;

/**
 * @author Maksim Radko
 */
public class MainPresenter {

    @NonNull
    private final MainView mView;

    public MainPresenter(@NonNull MainView view) {

        mView = view;
    }
}
