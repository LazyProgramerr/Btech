package com.sai.btech.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.sai.btech.R;

import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.agora.rtc2.Constants;
import io.agora.rtc2.RtcEngine;

public class VoiceCallActivity extends AppCompatActivity {

    String AGORA_CHANNEL_NAME;
    private RtcEngine rtcEngine;
    String appId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call);
        Bundle i = getIntent().getExtras();
        AGORA_CHANNEL_NAME = i.getString("chatRoomId");
        appId = "dcd1be87add74e718246bda20ce52817";
        initializeAgoraEngine();
        joinChannel();
        ImageButton end = findViewById(R.id.end);
        end.setOnClickListener(v -> leaveChannel());

    }

    private void joinC() {
        createChannel();
        joinChannel();
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
        rtcEngine.joinChannel(null, AGORA_CHANNEL_NAME, null,0);
    }

    private void createChannel() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("callRooms");

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