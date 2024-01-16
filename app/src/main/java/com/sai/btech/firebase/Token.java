package com.sai.btech.firebase;

import android.content.Context;

import com.google.firebase.messaging.FirebaseMessaging;
//import com.sai.btech.dialogs.loadingDialog;

public class Token {
    public interface TokenCallback {
        void onTokenReceived(String token);
    }

    public static void getToken(Context context, TokenCallback callback) {
//        loadingDialog LoadingDialog = new loadingDialog(context);
//        LoadingDialog.setCancelable(false);
//        LoadingDialog.show();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
//                    LoadingDialog.dismiss();
                    if (!task.isSuccessful()) {
                        if (callback != null) {
                            callback.onTokenReceived(""); // Notify the callback with an empty token
                        }
                        return;
                    }

                    String token = task.getResult();
                    if (callback != null) {
                        callback.onTokenReceived(token); // Notify the callback with the received token
                    }
                });
    }
}
