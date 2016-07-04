package com.example.inquallity.beacons.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.inquallity.beacons.R;
import com.example.inquallity.beacons.api.Bcons;
import com.example.inquallity.beacons.api.Provider;
import com.example.inquallity.beacons.model.BeaconResponse;
import com.example.inquallity.beacons.model.NamespacesResponse;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Maksim Radko
 */
public class SettingsActivity extends AppCompatActivity {

    private TextView mNamespace;

    private TextView mBeaconOne;

    @NonNull
    public static Intent makeIntent(@NonNull Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_settings);
        mNamespace = (TextView) findViewById(R.id.tvNamespace);
        mBeaconOne = (TextView) findViewById(R.id.tvBeaconOne);
        load();
    }

    private void load() {
        Provider.provide().create(Bcons.class)
                .getNamespaces()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NamespacesResponse>() {
                    @Override
                    public void call(NamespacesResponse namespacesResponse) {
                        mNamespace.setText(namespacesResponse.getBconNamespaces().get(0).getNamespaceName());
                    }
                });
        Provider.provide().create(Bcons.class)
                .getBeaconsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BeaconResponse>() {
                    @Override
                    public void call(BeaconResponse beaconResponse) {
                        final String name = beaconResponse.getBeacons().get(0).getBeaconName();
                        mBeaconOne.setText(name);
                    }
                });

    }
}
