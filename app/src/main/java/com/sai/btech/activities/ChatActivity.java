package com.sai.btech.activities;

import static com.sai.btech.notification.sendNotificationUsingOkhttp.sendNotification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sai.btech.R;
import com.sai.btech.adapters.ChatAdapter;
import com.sai.btech.models.ChatMessageModel;
import com.sai.btech.models.UserData;
import com.sai.btech.managers.SharedPreferenceManager;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private String chatRoomId,chatRoomName,chatRoomImg,chatRoomType;
    private ArrayList<String> chatRoomMembers;
    private EditText inputMsg;
    private DatabaseReference ChatRoomReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//        fetch chatRoom Details
        Intent intent = getIntent();
        chatRoomId = intent.getStringExtra("chatRoomId");
        chatRoomName = intent.getStringExtra("chatRoomName");
        chatRoomImg = intent.getStringExtra("chatRoomImg");
        chatRoomType = intent.getStringExtra("chatRoomType");
        chatRoomMembers = intent.getStringArrayListExtra("members");

//        fetch layout widgets
        ShapeableImageView chatRoomIconHolder = findViewById(R.id.profilePicView);
        TextView chatRoomNameHolder = findViewById(R.id.chatRoomNameHolder);
        RecyclerView recyclerView = findViewById(R.id.chat_recycler_view);
        inputMsg = findViewById(R.id.userMessage);

//        initialize the database
        ChatRoomReference = FirebaseDatabase.getInstance().getReference("ChatRooms");

//        set chatRoom
        chatRoomNameHolder.setText(chatRoomName);
        Glide.with(this).load(chatRoomImg).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_user_icon).into(chatRoomIconHolder);
        setChat(ChatRoomReference,recyclerView);
    }
    
//    displays the chat using recycleView
    private void setChat(DatabaseReference chatReference,RecyclerView recyclerView) {
        recyclerView.setItemViewCacheSize(200);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        ArrayList<ChatMessageModel> chatMessageModelArrayList = new ArrayList<>();
        ChatAdapter chatAdapter = new ChatAdapter(ChatActivity.this,chatMessageModelArrayList);
        if (chatRoomType.equals("group")){
            Query query = chatReference.child("groups").orderByChild("chatRoomId").equalTo(chatRoomId);
            query.addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatMessageModelArrayList.clear();
                    for (DataSnapshot data:snapshot.getChildren()) {
                       ChatMessageModel chatMessageModel = data.getValue(ChatMessageModel.class);
                       chatMessageModelArrayList.add(chatMessageModel);
                    }
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }
//    updates the data in chatRoom and calls function to send notification for all in the chatRoom
    public void sendMsg(){
        String msg = inputMsg.getText().toString().trim();
        UserData user = SharedPreferenceManager.getUserData(this);
        if (!msg.isEmpty()){
            String currentTimeMillis = String.valueOf(System.currentTimeMillis());
            DatabaseReference chatReference = ChatRoomReference.child(chatRoomType).child(chatRoomId);
            chatReference.child("msgTimeStamp").setValue(currentTimeMillis);
            chatReference.child("lastMsgSenderId").setValue(user.getUid());
            chatReference.child("msg").setValue(msg);

            ChatMessageModel chatMessageModel = new ChatMessageModel(msg, user.getUid(), currentTimeMillis);
            chatReference.child("chats").child(currentTimeMillis).setValue(chatMessageModel).addOnCompleteListener(task -> sendNotification(this,chatRoomId,msg,chatRoomImg,chatRoomName,chatRoomType,chatRoomMembers));
        }
    }
}