package com.example.inquallity.beacons.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.inquallity.beacons.R;
import com.example.inquallity.beacons.adapter.MessagesAdapter;
import com.example.inquallity.beacons.presenter.MainPresenter;
import com.example.inquallity.beacons.service.BeaconsMessageService;
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

public class MainActivity extends AppCompatActivity implements MainView, ResultCallback<Status> {

    private static final MessagesOptions MESSAGES_OPTIONS =
            new MessagesOptions.Builder().setPermissions(NearbyPermissions.BLE).build();

    private static final SubscribeOptions SUBSCRIBE_OPTIONS =
            new SubscribeOptions.Builder().setStrategy(Strategy.BLE_ONLY).build();

    private static final String TAG = MainActivity.class.getName();

    private static final int RC_ENABLE_BT = 1;

    private static final int RC_COARSE = 2;

    private MainPresenter mPresenter;

    private GoogleApiClient mApiClient;

    private View mLineIndicator;

    private RecyclerView mRecyclerView;

    private MessagesAdapter mAdapter = new MessagesAdapter();

    private BluetoothAdapter mBluetoothAdapter;

    private CheckBox mStaySubscribed;

    private boolean mIsSubscribed;

    @NonNull
    public static Intent makeIntent(@NonNull Context context) {
        return new Intent(context, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public void onProceedClick(View view) {
        if (!mBluetoothAdapter.isEnabled()) {
            Snackbar.make(mLineIndicator, R.string.bluetooth_disabled, Snackbar.LENGTH_LONG)
                    .setAction(R.string.enable, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, RC_ENABLE_BT);
                        }
                    })
                    .show();
        } else {
            subscribe();
        }
    }

    public void onShowListClick(View view) {
        startActivity(SettingsActivity.makeIntent(this));
    }

    @Override
    public void addMessage(Message message) {
        mAdapter.add(message);
    }

    @Override
    public void onResult(@NonNull Status status) {
        int textRes = 0;
        if (status.isSuccess()) {
            mIsSubscribed = true;
            textRes = R.string.subscribe_successful;
        } else if (status.isCanceled()) {
            mIsSubscribed = false;
            textRes = R.string.subscribe_cancelled;
        } else {
            mIsSubscribed = false;
            textRes = R.string.subscribe_error;
        }
        Snackbar.make(mLineIndicator, textRes, Snackbar.LENGTH_INDEFINITE).show();
        refreshBluetoothState();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            subscribe();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_ENABLE_BT) {
            refreshBluetoothState();
            subscribe();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        initViews();
        mPresenter = new MainPresenter(this);
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API, MESSAGES_OPTIONS)
                .addConnectionCallbacks(mPresenter)
                .enableAutoManage(this, mPresenter)
                .build();
        initAdapter();
    }

    @Override
    protected void onStop() {
        unSubscribe();
        super.onStop();
    }

    private void initViews() {
        mLineIndicator = findViewById(R.id.lineIndicator);
        mStaySubscribed = (CheckBox) findViewById(R.id.cbStaySubscribed);
        mStaySubscribed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mIsSubscribed) {
                        Nearby.Messages.unsubscribe(mApiClient, mPresenter);
                    }
                    final PendingIntent pendingIntent = PendingIntent.getService(
                            MainActivity.this, 0,
                            new Intent(MainActivity.this, BeaconsMessageService.class),
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    Nearby.Messages.subscribe(mApiClient, pendingIntent, SUBSCRIBE_OPTIONS).setResultCallback(MainActivity.this);
                } else {
                    Nearby.Messages.unsubscribe(mApiClient, mPresenter);
                }
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.rvMessages);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initAdapter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        } else {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        refreshBluetoothState();
    }

    private void refreshBluetoothState() {
        final int color = ContextCompat.getColor(this, mBluetoothAdapter.isEnabled()
                ? android.R.color.holo_green_dark
                : android.R.color.holo_red_dark);
        mLineIndicator.setBackgroundColor(color);
    }

    private void subscribe() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startService(BeaconsMessageService.makeIntent(getApplicationContext()));
//            if (mApiClient.isConnected()) {
//                Nearby.Messages.subscribe(mApiClient, mPresenter, SUBSCRIBE_OPTIONS).setResultCallback(this);
//            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, RC_COARSE);
        }
    }

    private void unSubscribe() {
        if (mApiClient.isConnected()) {
            Nearby.Messages.unsubscribe(mApiClient, mPresenter);
        }
    }

}
