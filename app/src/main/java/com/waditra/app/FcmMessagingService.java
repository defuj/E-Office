package com.waditra.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by defuj on 22/08/2016.
 */
public class FcmMessagingService extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(this,Notifikasi.class);
        intent.putExtra("judul",title);
        intent.putExtra("isi",message);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setSmallIcon(R.drawable.applogo);

        notificationBuilder.setSound(alarmSound);

        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(5,notificationBuilder.build());
    }
}
