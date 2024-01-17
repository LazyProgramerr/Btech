package com.sai.btech.managers;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sai.btech.models.ChatRoomModel;
import com.sai.btech.models.UserData;

import java.util.ArrayList;

public class ChatRoomManager {
    public static void CheckChatRoom(Context context,String chatRoomName, String chatRoomId, String chatRoomType, ArrayList<String> chatRoomMembers,String chatRoomImage){
        UserData ud = SharedPreferenceManager.getUserData(context);
        DatabaseReference chatRoomReferences = FirebaseDatabase.getInstance().getReference("chatRooms");
        DatabaseReference specificChatRoomReference = chatRoomReferences.child(chatRoomType).child(chatRoomId);

        specificChatRoomReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    // Chat room does not exist, create a new one
                    ChatRoomModel chatRoomModel = new ChatRoomModel(chatRoomId, String.valueOf(System.currentTimeMillis()),"",  ud.getuId(),chatRoomType,chatRoomName,chatRoomImage,chatRoomMembers);
                    specificChatRoomReference.setValue(chatRoomModel);
//                    Toast.makeText(context, "chatRoom Created", Toast.LENGTH_SHORT).show();
                }/* else {
                    // Chat room already exists
                    Toast.makeText(context, "ChatRoom already exists", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if necessary
            }
        });
    }
    public static String ChatRoomId(){return null;}
    public static String ChatRoomId(String user1,String user2){
        if (user1.hashCode()>user2.hashCode()){
            return user2+"_"+user1;
        }else return user1+"_"+user2;
    }
}
