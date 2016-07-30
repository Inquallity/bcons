package com.example.inquallity.beacons.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inquallity.beacons.R;
import com.google.android.gms.nearby.messages.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maksim Radko
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<Message> mMessages = new ArrayList<>();

    public void add(Message message) {
        mMessages.add(message);
        notifyItemChanged(mMessages.size() - 1);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.li_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.bindView(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }
}
