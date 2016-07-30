package com.example.inquallity.beacons.view;

import com.example.inquallity.beacons.model.BeaconInfo;

import java.util.List;

/**
 * @author Maksim Radko
 */
public interface SettingsView {

    void updateBeaconsList(List<BeaconInfo> beaconInfo);
}
