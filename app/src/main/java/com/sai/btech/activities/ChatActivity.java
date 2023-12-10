package com.sai.btech.activities;

import static com.sai.btech.firebaseUtil.CallApi.call;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sai.btech.AppFeatures.features;
import com.sai.btech.R;
import com.sai.btech.adapters.ChatAdapter;
import com.sai.btech.databinding.ActivityChatBinding;
import com.sai.btech.firebaseUtil.CallApi;
import com.sai.btech.models.ChatMessageModel;
import com.sai.btech.models.ChatRoomModel;
import com.sai.btech.models.UserData;
import com.sai.btech.sharedPreference.SharedPreferenceManager;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private DatabaseReference drChatroom;
    private DatabaseReference dRefer;
    private DatabaseReference cRefer;
    private ShapeableImageView profilePic;
    private FirebaseUser user;
    private String chatRoomId,Token;
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
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        assert receiverUid != null;
        assert user != null;
        chatRoomId = chatRoom(user.getUid(),receiverUid);
        drChatroom = FirebaseDatabase.getInstance().getReference("chatRooms");
        dRefer = FirebaseDatabase.getInstance().getReference("chatRooms/"+chatRoomId);
        cRefer = FirebaseDatabase.getInstance().getReference("chatRooms/"+chatRoomId+"/chats");
        setReceiverDetails(receiverName,receiverImg);
        getChatRoom(user.getUid(),receiverUid);
        binding.back.setOnClickListener(v -> onBackPressed());
        inputMsg.setOnClickListener(v -> recyclerView.scrollToPosition(adapter.getItemCount() - 1));
        sendBtn.setOnClickListener(v -> {
            String msg = inputMsg.getText().toString().trim();
            if (!msg.isEmpty()){
                sendMessage(msg);
               }
        });
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
        Query qu = db.orderByChild("uId").equalTo(receiverUid);
        qu.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot db:snapshot.getChildren()) {
                    Token = ""+db.child("token").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            @SuppressLint("NotifyDataSetChanged")
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
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        dRefer.child("msgTimeStamp").setValue(currentTimeMillis);
        dRefer.child("lastMsgSenderId").setValue(user.getUid());
        dRefer.child("msg").setValue(msg);


        ChatMessageModel chatMessageModel = new ChatMessageModel(msg,user.getUid(),currentTimeMillis);
        inputMsg.setText("");
        dRefer.child("/chats/"+currentTimeMillis).setValue(chatMessageModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                sendNotification(msg);
            }
        });
    }

    private void sendNotification(String msg) {
//        check once again
        UserData ud = SharedPreferenceManager.getUserData(this);
        try {
            JSONObject notificationObject = new JSONObject();
            notificationObject.put("title", ud.getuName());
            notificationObject.put("body", msg);

            JSONObject dataObject = new JSONObject();
            dataObject.put("userId", user.getUid());
            dataObject.put("userName", ud.getuName());
            dataObject.put("userImg", ud.getImg());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("notification", notificationObject);
             jsonObject.put("data", dataObject);
            jsonObject.put("to", Token);

            // Use AsyncTask to send the notification in the background
            new SendNotificationTask().execute(jsonObject);
        } catch (Exception e) {
            Log.i("json obj", e.toString());
            Toast.makeText(this, "123" + e, Toast.LENGTH_LONG).show();
        }
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
                            userUid,receiverUid,
                            String.valueOf(System.currentTimeMillis()),"",
                            userUid
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
            formatter = DateTimeFormatter.ofPattern("hh:mm a");
        }
        String formattedDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDateTime = currentDateTime.format(formatter);
        }


        return formattedDateTime;
    }
    private static class SendNotificationTask extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... jsonObjects) {
            // Perform network operation here, e.g., call(jsonObjects[0]);
            CallApi.call(jsonObjects[0]);
            return null;
        }

        // You can override onPostExecute to handle post-execution tasks if needed
    }
}
