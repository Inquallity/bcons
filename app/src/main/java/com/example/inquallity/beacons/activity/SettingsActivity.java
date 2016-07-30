package com.example.inquallity.beacons.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.inquallity.beacons.R;
import com.example.inquallity.beacons.adapter.BeaconInfoAdapter;
import com.example.inquallity.beacons.model.BeaconInfo;
import com.example.inquallity.beacons.presenter.SettingsPresenter;
import com.example.inquallity.beacons.view.SettingsView;

import java.util.List;

/**
 * @author Maksim Radko
 */
public class SettingsActivity extends AppCompatActivity implements SettingsView {

    private RecyclerView mRecyclerView;

    private SettingsPresenter mPresenter;

    private BeaconInfoAdapter mBeaconsAdapter = new BeaconInfoAdapter();

    @NonNull
    public static Intent makeIntent(@NonNull Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    public void updateBeaconsList(List<BeaconInfo> beaconInfo) {
        mBeaconsAdapter.changeDataSet(beaconInfo);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_settings);
        mPresenter = new SettingsPresenter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvBeacons);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mBeaconsAdapter);
        mPresenter.getBeaconsList();
    }
}
