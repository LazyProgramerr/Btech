package com.sai.btech;

import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MyReplyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
//        if (remoteInput != null) {
////            CharSequence replyText = remoteInput.getCharSequence(FirebaseNotification.KEY_TEXT_REPLY);
//
//            // Handle the user's reply here
//        }
    }
}

