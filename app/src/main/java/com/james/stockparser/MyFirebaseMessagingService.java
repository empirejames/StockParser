package com.james.stockparser;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData() != null) {
            Log.e(TAG, "title: " + remoteMessage.getData().get("title"));
            Log.e(TAG, "Notification Message Body: " + remoteMessage.getData().get("body"));
            sendNotification(remoteMessage);
        }



    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Log.e(TAG, "Start sendNotification");
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int uniqueId = (int) System.currentTimeMillis();
        String getTitle = remoteMessage.getData().get("title");
        String getMSG = remoteMessage.getData().get("body");
        //      Intent intent = new Intent(this, PushNewsActivity.class);
        //      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Intent intent = new Intent(getApplicationContext(), PushNewsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("pushTitle", getTitle);
        intent.putExtra("pushMsg", getMSG);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.notification_channel_id), "收推播訊息", importance);
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher96x96)
                    .setContentTitle(getTitle)
                    .setContentText(getMSG)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSound(defaultSoundUri)
                    .setChannelId(getResources().getString(R.string.notification_channel_id))
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pendingIntent);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
                notificationManager.notify(uniqueId, notification.build());
            }
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher96x96)
                    .setContentTitle(getTitle)
                    .setContentText(getMSG)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setSound(defaultSoundUri)
                    .setChannelId("stock")
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pendingIntent);

            notificationManager.notify(uniqueId, notificationBuilder.build());
        }
    }
}
