package com.example.inquallity.beacons.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.inquallity.beacons.R;
import com.example.inquallity.beacons.model.BeaconInfo;

/**
 * @author Maksim Radko
 */
public class BeaconHolder extends RecyclerView.ViewHolder {

    private TextView mBeaconName;

    private TextView mStatus;

    public BeaconHolder(View itemView) {
        super(itemView);
        mBeaconName = (TextView) itemView.findViewById(R.id.tvBeaconName);
        mStatus = (TextView) itemView.findViewById(R.id.tvStatus);
    }

    public void bindView(@NonNull BeaconInfo beaconInfo) {
        mBeaconName.setText(beaconInfo.getBeaconName());
        mStatus.setText(beaconInfo.getStatus());
    }
}
