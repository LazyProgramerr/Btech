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

import java.util.Objects;

public class firebaseData {
    public interface CallBack{
        void onDataReceived(String name);
    }
    public static void getData(Context context,String userId,CallBack callBack){
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
        Query q = userReference.orderByChild("uId").equalTo(userId);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds :snapshot.getChildren()){
                    String name = String.valueOf(ds.child("userName").getValue());
                    if (!name.isEmpty()){
                        callBack.onDataReceived(name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
