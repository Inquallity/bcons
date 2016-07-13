package com.example.inquallity.beacons.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.inquallity.beacons.R;
import com.example.inquallity.beacons.presenter.MainPresenter;
import com.example.inquallity.beacons.view.MainView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final MessagesOptions MESSAGES_OPTIONS =
            new MessagesOptions.Builder()
                    .setPermissions(NearbyPermissions.BLE)
                    .build();

    private static final SubscribeOptions SUBSCRIBE_OPTIONS =
            new SubscribeOptions.Builder()
                    .setStrategy(Strategy.BLE_ONLY)
                    .build();

    private MainPresenter mPresenter;

    private GoogleApiClient mApiClient;

    @NonNull
    public static Intent makeIntent(@NonNull Context context) {
        return new Intent(context, MainActivity.class);
    }

    public void onProceedClick(View view) {
        Nearby.Messages.subscribe(mApiClient, mPresenter, SUBSCRIBE_OPTIONS)
                .setResultCallback(new ResultCallback<Status>() {
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
    public void buildNotification(Message message) {
        final Notification nf = new NotificationCompat.Builder(this)
                .setContentTitle(message.getType())
                .setContentText(new String(message.getContent()))
                .setSmallIcon(android.R.drawable.ic_menu_gallery)
                .setContentIntent(PendingIntent.getActivities(this, 0, new Intent[]{MainActivity.makeIntent(this)}, PendingIntent.FLAG_ONE_SHOT))
                .setAutoCancel(true)
                .build();
        final NotificationManager ns = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        ns.notify(1, nf);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        mPresenter = new MainPresenter(this);

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
