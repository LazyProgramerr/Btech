package com.sai.btech.AppFeatures;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.snackbar.Snackbar;

public class features {
    public static void changeTheme(boolean isChecked) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    public static void SnackBar(View v, String message){
        Snackbar s = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
        s.show();
    }
}
