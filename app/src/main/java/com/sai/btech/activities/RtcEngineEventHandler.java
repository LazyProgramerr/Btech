package com.sai.btech.activities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import io.agora.rtc2.IRtcEngineEventHandler;

public class RtcEngineEventHandler extends IRtcEngineEventHandler {
    Context context;

    public RtcEngineEventHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onLeaveChannel(RtcStats stats) {
        Toast.makeText(context, "user left", Toast.LENGTH_SHORT).show();
        Log.i("left status","left");
        super.onLeaveChannel(stats);
    }


    @Override
    public void onUserJoined(int uid, int elapsed) {
        Toast.makeText(context, "joined : "+uid, Toast.LENGTH_SHORT).show();
        Log.i("joinStatus","joined"+uid);
        super.onUserJoined(uid, elapsed);
    }
}
