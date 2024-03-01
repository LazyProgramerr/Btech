package com.sai.btech.firebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sai.btech.R;
import com.sai.btech.activities.MyReplyReceiver;
import com.sai.btech.activities.VoiceCallActivity;

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
            String contentType = remoteMessage.getData().get("notificationInfoType");

            if (dataTitle != null && dataBody != null) {
                Log.d(TAG, "Data Title: " + dataTitle);
                Log.d(TAG, "Data Body: " + dataBody);
                if (notifyType.equals("call")) sendCallNotification(dataTitle);
                else sendNotification(dataTitle, dataBody, contentType);
            }
        }
    }

    private void sendCallNotification(String title) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel("userNotificationForTesting", "notification", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_call_notification_expanded);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.body, "call from " + title);

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
                        this, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
                        .setContentText("call from " + title)
                        .setSmallIcon(R.drawable.phone_icon)
                        .setCustomContentView(remoteViews)
                        .setCustomBigContentView(remoteViews)
                        .addAction(replyAction)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .build();

        // Show the notification
        notificationManager.notify(notificationId, notification);
    }


    private void sendNotification(String title, String messageBody, String contentType) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel("userNotificationForTesting", "notification", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_message_notification_expanded);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.body, messageBody);

        RemoteViews remoteViews1 = new RemoteViews(getPackageName(),R.layout.layout_message_notification_collapsed);
        remoteViews1.setTextViewText(R.id.title,title);
        remoteViews1.setTextViewText(R.id.body,messageBody);


        // Create a unique identifier for this notification
        int notificationId = (int) System.currentTimeMillis();

        // Create a PendingIntent for the reply action
        Bundle extras = new Bundle();
        extras.putString("userId", "12345");
        extras.putString("userName", "uName");
        extras.putString("userImg", "uImg");

        Intent replyIntent = new Intent(this, VoiceCallActivity.class);
        replyIntent.putExtras(extras);
        PendingIntent answerPendingIntent =
                PendingIntent.getBroadcast(
                        this, 0, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        // Create a RemoteInput for the reply action
        String replyLabel = "Reply";
        RemoteInput remoteInput = new RemoteInput.Builder("REPLAY").setLabel(replyLabel).build();

        remoteViews.setOnClickPendingIntent(R.id.answer, answerPendingIntent);

        // Create an action for the reply
        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(
                        android.R.drawable.ic_menu_send, replyLabel, answerPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        // Build the notification with the custom layout
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "userNotificationForTesting")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.chat_icon)
                .setCustomContentView(remoteViews1)
                .setCustomBigContentView(remoteViews)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setFullScreenIntent(answerPendingIntent,true);

        // Add the reply action only if the content type is "image"
        if (contentType.equals("image")) {
            // Load the image with Glide
            Glide.with(this).asBitmap().load(messageBody).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    remoteViews.setImageViewBitmap(R.id.image, resource);
                    remoteViews.setTextViewText(R.id.body, "");  // Assuming you want to hide the body after loading the image

                    remoteViews1.setTextViewText(R.id.body,"");

                    // Add the reply action after the image is loaded
                    notificationBuilder.addAction(replyAction);

                    // Show the notification
                    notificationManager.notify(notificationId, notificationBuilder.build());
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                    // Handle any cleanup if needed
                }
            });
        } else {
            remoteViews.setViewVisibility(R.id.image, View.GONE);
            remoteViews1.setViewVisibility(R.id.image, View.GONE);
            // Content type is not "image", show the notification without the image
            notificationManager.notify(notificationId, notificationBuilder.build());
        }
    }
}
