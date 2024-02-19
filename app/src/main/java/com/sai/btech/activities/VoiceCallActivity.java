package com.sai.btech.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sai.btech.R;
import com.sai.btech.managers.SharedPreferenceManager;
import com.sai.btech.models.UserData;

import java.util.ArrayList;
import java.util.Set;

import io.agora.rtc2.Constants;
import io.agora.rtc2.RtcEngine;

public class VoiceCallActivity extends AppCompatActivity {

    String AGORA_CHANNEL_NAME;
    private RtcEngine rtcEngine;
    DatabaseReference callRef;
    String appId;
    boolean isChannelCreated = false;
    ArrayList<String> receivers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call);

        Bundle i = getIntent().getExtras();
        AGORA_CHANNEL_NAME = i.getString("chatRoomId");
        String chatRoomType = i.getString("chatRoomType");
        String chatRoomImage = i.getString("chatRoomImage");
        receivers = i.getStringArrayList("receiversIds");
        String path = "chatRooms/" + chatRoomType + "/" + AGORA_CHANNEL_NAME;
        callRef = FirebaseDatabase.getInstance().getReference(path);
        appId = "dcd1be87add74e718246bda20ce52817";
        initializeAgoraEngine();
        ImageView imageView = findViewById(R.id.callDp);
        Glide.with(this).load(chatRoomImage).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        joinChannel();

        ImageButton end = findViewById(R.id.end);
        end.setOnClickListener(v -> {
            leaveChannel();
            finish();
        });
    }

    private void Join() {
        // Add your logic for joining the channel here
        // You might want to add the user ID to the database at this point
    }

    private void createChannelAndCheckForParticipants() {
        // Check if the channel is created in the database
        callRef.child("call/callChannelExists").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isChannelExists = snapshot.getValue(Boolean.class);
                if (!isChannelExists) {
                    // If the channel doesn't exist, create it and set the flag to true
                    callRef.child("call/callChannelExists").setValue(true);
                    isChannelCreated = true;

                    // Add the current user ID to the members node
                    UserData ud = SharedPreferenceManager.getUserData(VoiceCallActivity.this);
                    String currentUserId = ud.getuId(); // Replace with the actual user ID
                    callRef.child("call/members").child(currentUserId).setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if necessary
            }
        });

        // Check for participants in the members list
        callRef.child("call/members").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 1) {
                    // If more than one member exists, join the channel
                    Join();
                    joinChannel();
                } else {
                    // If only one member exists, wait for others to join
                    // You might want to implement a mechanism for waiting or notify the user
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if necessary
            }
        });

        // Check if all values of members are false only after someone joins the channel
        callRef.child("call/members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 1) {
                    // If more than one member exists, check if all values are false except the current user
                    boolean allFalseExceptCurrentUser = true;
                    String currentUserId = "your_current_user_id"; // Replace with the actual user ID
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (!ds.getKey().equals(currentUserId) && ds.getValue(Boolean.class)) {
                            allFalseExceptCurrentUser = false;
                            break;
                        }
                    }

                    // If all values are false except the current user, prompt to leave the channel
                    if (allFalseExceptCurrentUser) {
                        // Add your logic here to prompt the user to leave the channel
                        // You might want to show a dialog, toast, or handle it based on your UI/UX
                        // For example, you can call a method like promptUserToLeaveChannel()
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if necessary
            }
        });
    }


    private void initializeAgoraEngine() {
        try {
            rtcEngine = RtcEngine.create(getApplicationContext(), appId, new RtcEngineEventHandler());
            rtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void joinChannel() {
        rtcEngine.joinChannel(null, AGORA_CHANNEL_NAME, null, 0);
        setAudioOutput();
    }

    private void setAudioOutput() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : devices) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (device.getType() == BluetoothDevice.DEVICE_TYPE_DUAL && audioManager.isBluetoothScoOn()) {
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    audioManager.startBluetoothSco();
                    audioManager.setBluetoothScoOn(true);
                    audioManager.setSpeakerphoneOn(false);
                    break;
                }
            }
        }else{
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setSpeakerphoneOn(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leaveChannel();
        RtcEngine.destroy();
    }

    private void leaveChannel() {
        rtcEngine.leaveChannel();
    }
}
