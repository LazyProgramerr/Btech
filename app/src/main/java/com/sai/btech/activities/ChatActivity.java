package com.sai.btech.activities;

import static com.sai.btech.firebase.firebaseData.getFCMTokens;
import static com.sai.btech.managers.ChatRoomManager.CheckChatRoom;
import static com.sai.btech.notification.SendNotificationUsingVolley.sendFCMNotification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sai.btech.R;
import com.sai.btech.adapters.ChatAdapter;
import com.sai.btech.databinding.ActivityChatBinding;
import com.sai.btech.firebase.UploadToStorage;
import com.sai.btech.managers.SharedPreferenceManager;
import com.sai.btech.models.ChatMessageModel;
import com.sai.btech.models.UserData;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
            sendNotification(chatRoomName,"call");
        });
        EditText textMsg = findViewById(R.id.userMessage);
        ImageButton optionsPanel = findViewById(R.id.optionsPanel);
        textMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1){
                    optionsPanel.setVisibility(View.GONE);
                }else optionsPanel.setVisibility(View.VISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        optionsPanel.setOnClickListener(v-> {
            Toast.makeText(this, "clicked for options", Toast.LENGTH_SHORT).show();
            BottomSheetDialog dialog = new BottomSheetDialog(this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_options_panel,null);
            dialog.setContentView(view);
            dialog.show();

            ShapeableImageView opt1 = view.findViewById(R.id.option1);
            ShapeableImageView opt2 = view.findViewById(R.id.option2);
            ShapeableImageView opt3 = view.findViewById(R.id.option3);
            ShapeableImageView opt4 = view.findViewById(R.id.option4);
            ShapeableImageView opt5 = view.findViewById(R.id.option5);
            ShapeableImageView opt6 = view.findViewById(R.id.option6);

            opt1.setOnClickListener(v1 -> Toast.makeText(this, "opt1", Toast.LENGTH_SHORT).show());
            opt2.setOnClickListener(v2 -> pickImage());
            opt3.setOnClickListener(v3 -> Toast.makeText(this, "opt3", Toast.LENGTH_SHORT).show());
            opt4.setOnClickListener(v4 -> Toast.makeText(this, "opt4", Toast.LENGTH_SHORT).show());
            opt5.setOnClickListener(v5 -> Toast.makeText(this, "opt5", Toast.LENGTH_SHORT).show());
            opt6.setOnClickListener(v6 -> Toast.makeText(this, "opt6", Toast.LENGTH_SHORT).show());
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
            send(msg,String.valueOf(System.currentTimeMillis()),"text");
            sendNotification(msg,"chat");
        }
    }
    private void sendNotification(String body,String type) {
        UserData ud = SharedPreferenceManager.getUserData(ChatActivity.this);
        ArrayList<String> receivers = new ArrayList<>(chatRoomMembers);
        receivers.remove(ud.getuId());
        Toast.makeText(this, "msg sent", Toast.LENGTH_SHORT).show();
        getFCMTokens(this,receivers,tokens -> {
            sendFCMNotification(ChatActivity.this,tokens,ud.getName(),body,type);
            Toast.makeText(this, "notification sent", Toast.LENGTH_SHORT).show();
        });

    }

    private void send(String msg,String time,String msgType) {
        UserData ud = SharedPreferenceManager.getUserData(this);
        ChatMessageModel chatMessageModel = new ChatMessageModel(msg,ud.getuId(),time,ud.getUserImg(),msgType);
        chatReference.child(chatRoomId).child("chats").child(time).setValue(chatMessageModel);
        chatReference.child(chatRoomId).child("lastMsgSenderId").setValue(ud.getuId());
        if (msgType.equals("image")) chatReference.child(chatRoomId).child("msg").setValue("image");
        else chatReference.child(chatRoomId).child("msg").setValue(msg);
        chatReference.child(chatRoomId).child("msgTimeStamp").setValue(time);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void pickImage() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        } else {
            intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
        }
        openGalleryLauncher.launch(intent);
    }
    ActivityResultLauncher<Intent> openGalleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result ->{
                if (result.getResultCode() == Activity.RESULT_OK) {
                    assert result.getData() != null;
                    Uri imageUri = result.getData().getData();
                    assert imageUri != null;
                    UCrop.of(imageUri, Uri.fromFile(new File(this.getCacheDir(), "cropped_image.jpg")))
                            .start(this);
                }
            }
    );

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            assert data != null;
            Uri croppedImageUri = UCrop.getOutput(data);
             Bitmap userPhotoBitmap = uriToBitmap(croppedImageUri);
            assert userPhotoBitmap != null;
            String time = String.valueOf(System.currentTimeMillis());
            UploadToStorage.uploadImage(this, userPhotoBitmap, chatRoomId + "/" + time, new UploadToStorage.UploadImageCallback() {
                @Override
                public void onImageUpload(String imageUrl) {
                    send(imageUrl,time,"image");
                    sendNotification(imageUrl,"chat");
                    Toast.makeText(ChatActivity.this, "sent successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onImageUploadFailed(Exception e) {

                }
            });
        }
    }

    private Bitmap uriToBitmap(Uri croppedImageUri) {
        try {
            InputStream imageStream = this.getContentResolver().openInputStream(croppedImageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            if (imageStream != null) {
                imageStream.close();
            }
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}