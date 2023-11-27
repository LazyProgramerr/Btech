package com.sai.btech.AppFeatures;

import android.view.View;
import com.google.android.material.snackbar.Snackbar;

public class SnackBar {
    public void notifyMsg(View view, String message) {
        Snackbar s = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        s.show();
    }
}
