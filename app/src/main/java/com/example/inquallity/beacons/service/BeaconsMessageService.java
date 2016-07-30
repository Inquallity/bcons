package com.example.inquallity.beacons.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.inquallity.beacons.R;
import com.example.inquallity.beacons.activity.MainActivity;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

/**
 * @author Maksim Radko
 */
public class BeaconsMessageService extends IntentService {

    public BeaconsMessageService() {
        super("BEACON_SERVICE");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Context context = getApplicationContext();
        Nearby.Messages.handleIntent(intent, new MessageListener() {
            @Override
            public void onFound(Message message) {
                final Notification nf = new NotificationCompat.Builder(context)
                        .setContentTitle(message.getType())
                        .setContentText(new String(message.getContent()))
                        .setSmallIcon(R.drawable.ic_notification_white)
                        .setContentIntent(PendingIntent.getActivities(context, 0,
                                new Intent[]{MainActivity.makeIntent(context)}, PendingIntent.FLAG_ONE_SHOT))
                        .setAutoCancel(true)
                        .build();
                final NotificationManager ns = (NotificationManager) context.getApplicationContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                ns.notify(1, nf);
            }
        });
    }
}
