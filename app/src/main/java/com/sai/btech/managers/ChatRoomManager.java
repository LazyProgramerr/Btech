package com.sai.btech.managers;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sai.btech.models.ChatRoomModel;

import java.util.ArrayList;

public class ChatRoomManager {
//    check chatRoom exist or not
    public static void chatRoomStatus(String user1, String user2){
        String chatRoomId = createChatId(user1,user2);
        ArrayList<String> members = new ArrayList<>();
        members.add(user1);
        members.add(user2);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChatRooms");
        Query query = databaseReference.child("private").orderByChild("chatRoomId").equalTo(chatRoomId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // Chat room doesn't exist, create a new one
                     ChatRoomModel chatRoomModel = new ChatRoomModel(
                            chatRoomId,
                            members,
                            String.valueOf(System.currentTimeMillis()),"",
                            user1
                    );
                    databaseReference.child("private").child(chatRoomId).setValue(chatRoomModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public String createChatId(){
        String id = null;

        return id;
    }
    public static String createChatId(String u1, String u2){
        if (u1.hashCode()<u2.hashCode()){
            return ""+u1+"_"+u2;
        }
        else {
            return "" + u2 + "_" + u1;
        }
    }
}
