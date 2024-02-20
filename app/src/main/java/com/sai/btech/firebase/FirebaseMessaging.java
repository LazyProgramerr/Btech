package com.sai.btech.firebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sai.btech.R;
import com.sai.btech.activities.MyReplyReceiver;
import com.sai.btech.activities.VoiceCallActivity;
import com.sai.btech.activities.WelcomeActivity;

import java.util.HashMap;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class FirebaseMessaging extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String CHANNEL_ID = "Your_Channel_ID"; // Replace with your channel ID

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a data payload
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            // Handle the data payload here
            String dataTitle = remoteMessage.getData().get("title");
            String dataBody = remoteMessage.getData().get("body");
            String notifyType = remoteMessage.getData().get("NotifyType");

            if (dataTitle != null && dataBody != null) {
                Log.d(TAG, "Data Title: " + dataTitle);
                Log.d(TAG, "Data Body: " + dataBody);
                if (notifyType.equals("call")) sendCallNotification(dataTitle);
                else sendNotification(dataTitle, dataBody);
            }
        }
    }

    private void sendCallNotification(String title) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel("userNotificationForTesting","notification",NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);

        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification_expand_layout);
        remoteViews.setTextViewText(R.id.title,title);
        remoteViews.setTextViewText(R.id.body, "call from "+title);

        // Create a unique identifier for this notification
        int notificationId = (int) System.currentTimeMillis();

        // Create a PendingIntent for the reply action
        Bundle extras = new Bundle();
        extras.putString("userId", "12345");
        extras.putString("userName", "uName");
        extras.putString("userImg", "uImg");

        Intent replyIntent = new Intent(this, MyReplyReceiver.class);
        replyIntent.putExtras(extras);
        replyIntent.putExtra("notification_id", notificationId);
        PendingIntent replyPendingIntent =
                PendingIntent.getBroadcast(
                        this, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        // Create a RemoteInput for the reply action
        String replyLabel = "Reply";
        RemoteInput remoteInput = new RemoteInput.Builder("REPLAY").setLabel(replyLabel).build();

        // Create an action for the reply
        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(
                        android.R.drawable.ic_menu_send, replyLabel, replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        // Build the notification with the custom layout and reply action
        Notification notification =
                new NotificationCompat.Builder(this, "userNotificationForTesting")
                        .setContentTitle(title)
                        .setContentText("call from "+title)
                        .setSmallIcon(R.drawable.phone_icon)
                        .setCustomContentView(remoteViews)
                        .setCustomBigContentView(remoteViews)
                        .addAction(replyAction)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .build();

        // Show the notification
        notificationManager.notify(notificationId,notification);
    }


    private void sendNotification(String title, String messageBody) {
        /*Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.chat_icon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create the notification channel (for Android Oreo and above)
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);

        notificationManager.notify(0, notificationBuilder.build());*/
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel("userNotificationForTesting","notification",NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);

        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification_expand_layout);
        remoteViews.setTextViewText(R.id.title,title);
        remoteViews.setTextViewText(R.id.body, messageBody);

        // Create a unique identifier for this notification
        int notificationId = (int) System.currentTimeMillis();

        // Create a PendingIntent for the reply action
        Bundle extras = new Bundle();
        extras.putString("userId", "12345");
        extras.putString("userName", "uName");
        extras.putString("userImg", "uImg");

        Intent replyIntent = new Intent(this, VoiceCallActivity.class);
        replyIntent.putExtras(extras);
//        replyIntent.putExtra("notification_id", notificationId);
        PendingIntent answerPendingIntent =
                PendingIntent.getBroadcast(
                        this, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        // Create a RemoteInput for the reply action
        String replyLabel = "Reply";
        RemoteInput remoteInput = new RemoteInput.Builder("REPLAY").setLabel(replyLabel).build();

        remoteViews.setOnClickPendingIntent(R.id.answer,answerPendingIntent);

        // Create an action for the reply
        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(
                        android.R.drawable.ic_menu_send, replyLabel, answerPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        // Build the notification with the custom layout and reply action
        Notification notification =
                new NotificationCompat.Builder(this, "userNotificationForTesting")
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setSmallIcon(R.drawable.chat_icon)
                        .setCustomContentView(remoteViews)
                        .setCustomBigContentView(remoteViews)
                        .addAction(replyAction)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .build();

        // Show the notification
        notificationManager.notify(notificationId,notification);
    }
}
