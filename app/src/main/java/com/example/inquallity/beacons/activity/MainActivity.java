package com.example.inquallity.beacons.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inquallity.beacons.R;
import com.example.inquallity.beacons.presenter.MainPresenter;
import com.example.inquallity.beacons.view.MainView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final MessagesOptions MESSAGES_OPTIONS = new MessagesOptions.Builder()
            .setPermissions(NearbyPermissions.BLE).build();

    private static final SubscribeOptions SUBSCRIBE_OPTIONS = new SubscribeOptions.Builder().setStrategy(Strategy.BLE_ONLY).build();

    private MainPresenter mPresenter;

    private GoogleApiClient mApiClient;

    private TextView mMessages;

    @NonNull
    public static Intent makeIntent(@NonNull Context context) {
        return new Intent(context, MainActivity.class);
    }

    public void onProceedClick(View view) {
        Nearby.Messages.subscribe(mApiClient, mPresenter, SUBSCRIBE_OPTIONS).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    Toast.makeText(MainActivity.this, "Subscribed successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onShowListClick(View view) {
        startActivity(SettingsActivity.makeIntent(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        mPresenter = new MainPresenter(this);
        mMessages = (TextView) findViewById(R.id.tvMessages);

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API, MESSAGES_OPTIONS)
                .addConnectionCallbacks(mPresenter)
                .enableAutoManage(this, mPresenter)
                .build();
    }

    @Override
    protected void onStop() {
        if (mApiClient.isConnected()) {
            Nearby.Messages.unsubscribe(mApiClient, mPresenter);
        }
        super.onStop();
    }

}
