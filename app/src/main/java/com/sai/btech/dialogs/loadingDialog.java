package com.sai.btech.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.sai.btech.R;

public class loadingDialog extends Dialog {
    public loadingDialog(@NonNull Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        LottieAnimationView lottieAnimationView = findViewById(R.id.animationView);
        lottieAnimationView.playAnimation();

    }
}
