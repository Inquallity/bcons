package com.example.inquallity.beacons.presenter;

import android.support.annotation.NonNull;

import com.example.inquallity.beacons.api.Bcons;
import com.example.inquallity.beacons.api.Provider;
import com.example.inquallity.beacons.model.BeaconInfo;
import com.example.inquallity.beacons.model.BeaconResponse;
import com.example.inquallity.beacons.view.SettingsView;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Maksim Radko
 */
public class SettingsPresenter {

    @NonNull
    private final SettingsView mView;

    public SettingsPresenter(@NonNull SettingsView view) {

        mView = view;
    }

    public void getBeaconsList() {
        Provider.provide().create(Bcons.class)
                .getBeaconsList()
                .map(new Func1<BeaconResponse, List<BeaconInfo>>() {
                    @Override
                    public List<BeaconInfo> call(BeaconResponse beaconResponse) {
                        return beaconResponse.getBeacons();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<BeaconInfo>>() {
                    @Override
                    public void call(List<BeaconInfo> beacons) {
                        mView.updateBeaconsList(beacons);
                    }
                });
    }
}
