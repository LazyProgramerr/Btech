package com.sai.btech.activities;

import static com.sai.btech.firebase.firebaseData.getFCMTokens;
import static com.sai.btech.managers.ChatRoomManager.CheckChatRoom;
import static com.sai.btech.notification.SendNotificationUsingVolley.sendFCMNotification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sai.btech.R;
import com.sai.btech.adapters.ChatAdapter;
import com.sai.btech.databinding.ActivityChatBinding;
import com.sai.btech.managers.SharedPreferenceManager;
import com.sai.btech.models.ChatMessageModel;
import com.sai.btech.models.UserData;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding layoutWidgets;
    DatabaseReference chatReference;
    String chatRoomImage,chatRoomType,chatRoomId,chatRoomName;
    ArrayList<String> chatRoomMembers = new ArrayList<>();
    ChatAdapter chatAdapter;
    List<ChatMessageModel> chatMessageModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutWidgets = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(layoutWidgets.getRoot());

        Intent i = getIntent();
        chatRoomImage = i.getStringExtra("chatRoomImage");
        chatRoomName = i.getStringExtra("chatRoomName");
        chatRoomId = i.getStringExtra("chatRoomId");
        chatRoomType = i.getStringExtra("chatRoomType");
        chatRoomMembers = i.getStringArrayListExtra("chatRoomMembers");

        CheckChatRoom(this,chatRoomName,chatRoomId,chatRoomType,chatRoomMembers,chatRoomImage);

//        set chatroom
        layoutWidgets.chatRoomName.setText(chatRoomName);
        Glide.with(this).load(chatRoomImage).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_user_icon).into(layoutWidgets.chatRoomImage.getRoot());
        chatMessageModelList = new ArrayList<>();

        assert chatRoomType != null;
        chatReference = FirebaseDatabase.getInstance().getReference("chatRooms").child(chatRoomType);

        setChat();
        layoutWidgets.call.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), VoiceCallActivity.class);
            intent.putExtra("chatRoomId",chatRoomId);
            intent.putExtra("chatRoomImage",chatRoomImage);
            intent.putExtra("chatRoomType",chatRoomType);
            startActivity(intent);
        });

    }

    private void setChat() {
        layoutWidgets.chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(this,chatRoomType,chatMessageModelList);
        layoutWidgets.chatRecyclerView.setAdapter(chatAdapter);
        Query query = chatReference.child(chatRoomId).child("chats");
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMessageModelList.clear();
                for (DataSnapshot data:snapshot.getChildren()) {
                    ChatMessageModel chatMessageModel = data.getValue(ChatMessageModel.class);
                    chatMessageModelList.add(chatMessageModel);
                }
                chatAdapter.notifyDataSetChanged();
                layoutWidgets.chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void sendMsg(View view){
        EditText inputMsg = findViewById(R.id.userMessage);
        String msg = inputMsg.getText().toString().trim();
        if (!msg.isEmpty()){
            inputMsg.setText(null);
            send(msg,String.valueOf(System.currentTimeMillis()));
            sendNotification(msg);
        }
    }

    private void sendNotification(String body) {
        UserData ud = SharedPreferenceManager.getUserData(ChatActivity.this);
        ArrayList<String> receivers = new ArrayList<>(chatRoomMembers);
        receivers.remove(ud.getuId());
        Toast.makeText(this, "msg sent", Toast.LENGTH_SHORT).show();
        getFCMTokens(this,receivers,tokens -> {
            sendFCMNotification(ChatActivity.this,tokens,ud.getName(),body);
            Toast.makeText(this, "notification sent", Toast.LENGTH_SHORT).show();
        });

    }

    private void send(String msg,String time) {
        UserData ud = SharedPreferenceManager.getUserData(this);
        ChatMessageModel chatMessageModel = new ChatMessageModel(msg,ud.getuId(),time,ud.getUserImg());
        chatReference.child(chatRoomId).child("chats").child(time).setValue(chatMessageModel);
        chatReference.child(chatRoomId).child("lastMsgSenderId").setValue(ud.getuId());
        chatReference.child(chatRoomId).child("msg").setValue(msg);
        chatReference.child(chatRoomId).child("msgTimeStamp").setValue(time);
    }
}