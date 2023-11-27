package com.sai.btech.dialogs;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

public class ProfileDialog extends Dialog {
    private String photoUrl;
    private Context context;

    public ProfileDialog(@NonNull Context context, String photoUrl) {
        super(context);
        this.photoUrl = photoUrl;

    }
}
