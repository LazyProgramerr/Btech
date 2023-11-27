package com.sai.btech.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sai.btech.R;
import com.sai.btech.adapters.ChatAdapter;
import com.sai.btech.databinding.ActivityChatBinding;
import com.sai.btech.models.ChatMessageModel;
import com.sai.btech.models.ChatRoomModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private DatabaseReference drReceiver,drChatroom,dRefer,cRefer;
    private ShapeableImageView profilePic;
    private FirebaseUser user;
    private String chatRoomId;
    private EditText inputMsg;
    ChatRoomModel chatRoomModel;
    RecyclerView recyclerView;
    ArrayList<ChatMessageModel> chatMessageModelArrayList;
    ChatAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        profilePic = findViewById(R.id.profilePicView);
        ImageView sendBtn = findViewById(R.id.sendBtn);
        inputMsg = findViewById(R.id.userMessage);


        Intent intent = getIntent();
        String receiverUid = intent.getStringExtra("receiverUid");
        String receiverName = intent.getStringExtra("receiverName");
        String receiverImg = intent.getStringExtra("receiverImg");
        Toast.makeText(this, ""+receiverUid, Toast.LENGTH_SHORT).show();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        chatRoomId = chatRoom(user.getUid(),receiverUid);
        drReceiver = FirebaseDatabase.getInstance().getReference("Users");
        drChatroom = FirebaseDatabase.getInstance().getReference("chatRooms");
        dRefer = FirebaseDatabase.getInstance().getReference("chatRooms/"+chatRoomId);
        cRefer = FirebaseDatabase.getInstance().getReference("chatRooms/"+chatRoomId+"/chats");
        setReceiverDetails(receiverName,receiverImg);
        getChatRoom(user.getUid(),receiverUid);
        binding.back.setOnClickListener(v -> {
            onBackPressed();
        });
        sendBtn.setOnClickListener(v -> {
            String msg = inputMsg.getText().toString().trim();
            if (!msg.isEmpty()){
                sendMessage(msg);
            }
        });
        setChat();
    }

    private void setChat() {
        recyclerView = findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatMessageModelArrayList = new ArrayList<>();
        adapter = new ChatAdapter(this,chatMessageModelArrayList);
        recyclerView.setAdapter(adapter);


        cRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatMessageModelArrayList.clear();
                for (DataSnapshot ds:snapshot.getChildren()) {
                    ChatMessageModel chatMessageModel = ds.getValue(ChatMessageModel.class);
                    chatMessageModelArrayList.add(chatMessageModel);
                }
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String msg) {
        dRefer.child("msgTimeStamp").setValue(Time());
        dRefer.child("lastMsgSenderId").setValue(user.getUid());
        long currentTimeMillis = System.currentTimeMillis();

        ChatMessageModel chatMessageModel = new ChatMessageModel(msg,user.getUid(),Time());
        dRefer.child("/chats/"+currentTimeMillis).setValue(chatMessageModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                inputMsg.setText("");
            }
        });
    }

    private void getChatRoom(String userUid, String receiverUid) {

        Query query = drChatroom.orderByChild("ChatroomId").equalTo(chatRoomId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Chat room doesn't exist, create a new one
                    Toast.makeText(ChatActivity.this, "chatroom created", Toast.LENGTH_SHORT).show();
                    chatRoomModel = new ChatRoomModel(
                            chatRoomId,
                            Arrays.asList(userUid, receiverUid),
                            Time(),
                            ""
                    );
                    drChatroom.child(chatRoomId).setValue(chatRoomModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }

    private String chatRoom(String userUid,String receiverUid){
        if (userUid.hashCode()<receiverUid.hashCode()){
            return ""+userUid+"_"+receiverUid;
        }
        else {
            return "" + receiverUid + "_" + userUid;
        }
    }

    private void setReceiverDetails(String receiverName,String receiverImg){
        binding.receiverName.setText(receiverName);
        try {
            Glide.with(ChatActivity.this)
                    .load(receiverImg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.default_user_icon)
                    .into(profilePic);
        }catch (Exception e){
            Glide.with(ChatActivity.this)
                    .load(R.drawable.default_user_icon)
                    .into(profilePic);
        }
    }
    public static String Time() {
        // Get the current date and time
        LocalDateTime currentDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDateTime = LocalDateTime.now();
        }

        // Format the date and time using a specific pattern
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm a");
        }
        String formattedDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDateTime = currentDateTime.format(formatter);
        }

        return formattedDateTime;
    }
}