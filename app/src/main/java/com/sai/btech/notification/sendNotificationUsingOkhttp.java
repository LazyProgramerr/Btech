package com.sai.btech.notification;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sai.btech.models.UserData;
import com.sai.btech.managers.SharedPreferenceManager;

import org.json.JSONObject;

import java.util.ArrayList;

public class sendNotificationUsingOkhttp {
    public static void sendNotification(Context context, String chatRoomId, String msg,
                                        String chatRoomImg, String chatRoomName,String chatRoomType,
                                        ArrayList<String> chatRoomReceivers){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
        UserData ud = SharedPreferenceManager.getUserData(context);
        for (String receiver:chatRoomReceivers) {
            if (ud.getUid().equals(receiver)){continue;}
            Query qu = db.orderByChild("uId").equalTo(receiver);
            qu.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot db:snapshot.getChildren()) {
                        try {
                            JSONObject notificationObject = new JSONObject();
                            notificationObject.put("title", chatRoomName+":"+ud.getuName());
                            notificationObject.put("body", msg);

                            JSONObject dataObject = new JSONObject();
                            dataObject.put("chatRoomId", chatRoomId);
                            dataObject.put("chatRoomName", chatRoomName);
                            dataObject.put("chatRoomImg", chatRoomImg);
                            dataObject.put("chatRoomType",chatRoomType);

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("notification", notificationObject);
                            jsonObject.put("data", dataObject);
                            jsonObject.put("to", ""+db.child("token").getValue());

//                            // Use AsyncTask to send the notification in the background
//                            new ChatActivity.SendNotificationTask().execute(jsonObject);
                            CallApi.send(jsonObject);
                        } catch (Exception e) {
                            Log.i("json obj", e.toString());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private static class SendNotificationTask extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... jsonObjects) {
            // Perform network operation here, e.g., call(jsonObjects[0]);
            CallApi.send(jsonObjects[0]);
            return null;
        }
    }
}
