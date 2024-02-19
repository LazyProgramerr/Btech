package com.sai.btech.activities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import io.agora.rtc2.IRtcEngineEventHandler;

public class RtcEngineEventHandler extends IRtcEngineEventHandler {
    Context context;
    @Override
    public void onLeaveChannel(RtcStats stats) {
//        HomeActivity.showMessage("user left");
        Log.i("left status","left");
        super.onLeaveChannel(stats);
    }


    @Override
    public void onUserJoined(int uid, int elapsed) {
//        HomeActivity.showMessage("joined : "+uid);
        Log.i("joinStatus","joined"+uid);
        super.onUserJoined(uid, elapsed);
    }
}
