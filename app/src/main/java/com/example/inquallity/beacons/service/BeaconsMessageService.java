package com.example.inquallity.beacons.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.eddystone.Eddystone;

import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Radko
 */
public class BeaconsMessageService extends Service {

    private static final String TAG = BeaconsMessageService.class.getSimpleName();

    private BeaconManager mBeaconManager;

    private List<Eddystone> mUuids = Collections.emptyList();

    public static Intent makeIntent(Context ctx) {
        return new Intent(ctx, BeaconsMessageService.class);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBeaconManager = new BeaconManager(this);
        mBeaconManager.setForegroundScanPeriod(2_000L, 30_000L);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                startMonitoring();
            }
        });
        return START_STICKY;
    }

    private void startMonitoring() {
        mBeaconManager.setEddystoneListener(new BeaconManager.EddystoneListener() {
            @Override
            public void onEddystonesFound(List<Eddystone> list) {
                if (list == null || list.isEmpty()) {
                    Log.d(TAG, "onEddystonesFound: NOT_FOUND");
                }
                for (Eddystone eddystone : list) {
                    if (!mUuids.contains(eddystone)) {
                        Log.d(TAG, "onEddystone Found: do request");
                    }
                }
                mUuids = Collections.unmodifiableList(list);
            }
        });
        mBeaconManager.startEddystoneScanning();
    }

}
