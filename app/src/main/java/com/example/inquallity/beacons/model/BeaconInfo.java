package com.example.inquallity.beacons.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Maksim Radko
 */
public class BeaconInfo {

    @SerializedName("advertisedId")
    private AdvertisedId mAdvertisedId;

    @SerializedName("beaconName")
    private String mBeaconName;

    @SerializedName("status")
    private String mStatus;

    public AdvertisedId getAdvertisedId() {
        return mAdvertisedId;
    }

    public String getBeaconName() {
        return mBeaconName;
    }

    public String getStatus() {
        return mStatus;
    }
}
