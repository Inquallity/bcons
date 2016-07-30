package com.example.inquallity.beacons.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.inquallity.beacons.view.MainView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Maksim Radko
 */
public class MainPresenter extends MessageListener implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @NonNull
    private final MainView mView;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

    public MainPresenter(@NonNull MainView view) {

        mView = view;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onFound(Message message) {
        mView.buildNotification(message);
        mView.addMessage(message);
    }
}
