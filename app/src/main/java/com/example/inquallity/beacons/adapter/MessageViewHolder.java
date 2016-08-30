package com.example.inquallity.beacons.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.inquallity.beacons.R;
import com.google.android.gms.nearby.messages.Message;

/**
 * @author Maksim Radko
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {

    private TextView mNamespace;

    private TextView mType;

    private TextView mContent;

    private TextView mDeviceId;

    public MessageViewHolder(View itemView) {
        super(itemView);
        mNamespace = (TextView) itemView.findViewById(R.id.tvNamespace);
        mType = (TextView) itemView.findViewById(R.id.tvType);
        mContent = (TextView) itemView.findViewById(R.id.tvContent);
        mDeviceId = (TextView) itemView.findViewById(R.id.tvDeviceId);
    }

    public void bindView(Message message) {
        mNamespace.setText(message.getNamespace());
        mType.setText(message.getType());
        mContent.setText(new String(message.getContent()));
    }
}
