package com.example.inquallity.beacons.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.inquallity.beacons.R;

/**
 * @author Maksim Radko
 */
public class ProbeActivity extends AppCompatActivity {

    @NonNull
    public static Intent makeIntent(@NonNull Context context) {
        return new Intent(context, ProbeActivity.class);
    }

    public void onProbeClick(View view) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_probe);

    }
}
