package com.example.inquallity.beacons.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.inquallity.beacons.R;

/**
 * @author Maksim Radko
 */
public class InfoActivity extends AppCompatActivity {

    private static final String KEY_BEACON_NAME = "BEACON_NAME";

    @NonNull
    public static Intent makeIntent(@NonNull Context context, @NonNull String beaconName) {
        return new Intent(context, InfoActivity.class).putExtra(KEY_BEACON_NAME, beaconName);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_info);
    }
}
