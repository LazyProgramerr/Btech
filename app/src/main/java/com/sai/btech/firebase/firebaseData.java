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
import java.util.HashMap;
import java.util.Objects;

public class firebaseData {
    public interface DataCallBack{
        void onDataReceived(HashMap<String,String> data);
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
                    String image = String.valueOf(ds.child("userImg").getValue());
                    if (!name.isEmpty() && !image.isEmpty()){
                        HashMap<String,String> data = new HashMap<>();
                        data.put("name",name);
                        data.put("image",image);
                        callBack.onDataReceived(data);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    public static void getFCMTokens(Context context, ArrayList<String> members, FCMCallBack callBack) {
        DatabaseReference tokenReference = FirebaseDatabase.getInstance().getReference("userTokens");
        ArrayList<String> fcmTokens = new ArrayList<>();

        tokenReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (String memberId : members) {
                    String fcmToken;
                    if (memberId != null){
                        fcmToken = String.valueOf(snapshot.child(memberId).getValue());
                        if (!fcmToken.isEmpty() && !fcmTokens.contains(fcmToken)) {
                            fcmTokens.add(fcmToken);
                        }
                    }
                    // Check if the token is not empty
                }

                // Check if we have fetched FCM tokens for all members
                if (fcmTokens.size() == members.size()) {
                    callBack.onTokensReceived(fcmTokens);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });
    }
}