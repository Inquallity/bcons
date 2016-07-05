package com.example.inquallity.beacons.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inquallity.beacons.R;
import com.example.inquallity.beacons.model.BeaconInfo;

import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Radko
 */
public class BeaconInfoAdapter extends RecyclerView.Adapter<BeaconHolder> {

    private List<BeaconInfo> mInfo = Collections.emptyList();

    public void changeDataSet(List<BeaconInfo> beaconInfo) {
        mInfo = Collections.unmodifiableList(beaconInfo);
        notifyDataSetChanged();
    }

    @Override
    public BeaconHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.li_beacon, parent, false);
        return new BeaconHolder(view);
    }

    @Override
    public void onBindViewHolder(BeaconHolder holder, int position) {
        holder.bindView(mInfo.get(position));
    }

    @Override
    public int getItemCount() {
        return mInfo.size();
    }

    @NonNull
    public BeaconInfo getItem(int position) {
        return mInfo.get(position);
    }
}
