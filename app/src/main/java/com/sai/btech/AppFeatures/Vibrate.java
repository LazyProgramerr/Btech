package com.sai.btech.AppFeatures;

import android.content.Context;
import android.os.Vibrator;

import com.sai.btech.models.VibrateStatus;
import com.sai.btech.managers.SharedPreferenceManager;

public class Vibrate {
    public static void vibratePhone(Context context, long milliSeconds){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        VibrateStatus vS = SharedPreferenceManager.getVibrateStatus(context);
        if (vibrator != null && vS.getVibrateStatus()){
            vibrator.vibrate(milliSeconds);
        }
    }
}
