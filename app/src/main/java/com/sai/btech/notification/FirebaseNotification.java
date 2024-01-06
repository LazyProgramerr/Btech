package com.sai.btech.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.annotation.WorkerThread;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sai.btech.MyReplyReceiver;
import com.sai.btech.R;
import com.sai.btech.constants.btech;

public class FirebaseNotification extends FirebaseMessagingService {
    String receiverImage;

    @Override
    @WorkerThread
    public void onMessageReceived(RemoteMessage msg) {
        super.onMessageReceived(msg);
        String title = msg.getNotification().getTitle();
        String body = msg.getNotification().getBody();
        if (msg.getData() != null) {
            String receiverName = msg.getData().get("userName");
            receiverImage = msg.getData().get("userImg");
            String userId = msg.getData().get("userId");
            notify(title, body, userId, receiverName, receiverImage);
        }
    }

    private void notify(String title, String body, String uid, String uName, String uImg) {
        NotificationManager notificationM =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel =
                null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                    "my_channel_id", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationM.createNotificationChannel(channel);
        }

        // Create RemoteViews for the custom layout
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        remoteViews.setTextViewText(R.id.notification_title, title);
        remoteViews.setTextViewText(R.id.notification_message, body);

        // Create a unique identifier for this notification
        int notificationId = (int) System.currentTimeMillis();

        // Create a PendingIntent for the reply action
        Bundle extras = new Bundle();
        extras.putString("userId", uid);
        extras.putString("userName", uName);
        extras.putString("userImg", uImg);

        Intent replyIntent = new Intent(this, MyReplyReceiver.class);
        replyIntent.putExtras(extras);
        replyIntent.putExtra("notification_id", notificationId);
        PendingIntent replyPendingIntent =
                PendingIntent.getBroadcast(
                        this, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        // Create a RemoteInput for the reply action
        String replyLabel = "Reply";
        RemoteInput remoteInput = new RemoteInput.Builder(btech.REPLAY).setLabel(replyLabel).build();

        // Create an action for the reply
        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(
                        android.R.drawable.ic_menu_send, replyLabel, replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        // Build the notification with the custom layout and reply action
        Notification notification =
                new NotificationCompat.Builder(this, "my_channel_id")
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setCustomContentView(remoteViews)
                        .addAction(replyAction)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .build();

        // Show the notification
        startForeground(notificationId, notification);
    }
}
