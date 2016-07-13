package com.example.inquallity.beacons.view;

import com.google.android.gms.nearby.messages.Message;

/**
 * @author Maksim Radko
 */
public interface MainView {

    void buildNotification(Message message);
}
