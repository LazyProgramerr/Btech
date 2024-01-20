package com.sai.btech.firebase;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sai.btech.models.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class firebaseData {
    public interface DataCallBack{
        void onDataReceived(String name);
    }public interface FCMCallBack{
        void onTokensReceived(ArrayList<String> tokens);
    }
    public static void getData(Context context,String userId,DataCallBack callBack){
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
        Query q = userReference.orderByChild("uId").equalTo(userId);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds :snapshot.getChildren()){
                    String name = String.valueOf(ds.child("Name").getValue());
                    if (!name.isEmpty()){
                        callBack.onDataReceived(name);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    public static void getFCMTokens(Context context, ArrayList<String> members, FCMCallBack callBack) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
        ArrayList<String> fcmTokens = new ArrayList<>();
        // Iterate through the list of member IDs
        for (String memberId : members) {
            Query q = userReference.orderByChild("uId").equalTo(memberId);
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        // Assuming "fcmToken" is the key where FCM token is stored in the database
                        String fcmToken = String.valueOf(ds.child("Token").getValue());
                        // Check if the FCM token is not empty and not already in the list
                        if (!fcmToken.isEmpty() && !fcmTokens.contains(fcmToken)) {
                            fcmTokens.add(fcmToken);
                        }
                    }
                    // Check if we have fetched FCM tokens for all members
                    if (fcmTokens.size() == members.size()) {
                        callBack.onTokensReceived(fcmTokens);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }


}
