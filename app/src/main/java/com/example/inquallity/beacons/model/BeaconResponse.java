package com.example.inquallity.beacons.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Maksim Radko
 */
public class BeaconResponse {

    @SerializedName("beacons")
    private List<BeaconInfo> mBeacons;

    @SerializedName("nextPageToken")
    private String mNextPageToken;

    @SerializedName("totalCount")
    private String mTotalCount;

    public List<BeaconInfo> getBeacons() {
        return mBeacons;
    }

    public String getNextPageToken() {
        return mNextPageToken;
    }

    public String getTotalCount() {
        return mTotalCount;
    }
}
