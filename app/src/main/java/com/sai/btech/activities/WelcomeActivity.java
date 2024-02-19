package com.sai.btech.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sai.btech.authentication.LoginActivity;
import com.sai.btech.databinding.ActivityWelcomeBinding;
import com.sai.btech.managers.SharedPreferenceManager;
import com.sai.btech.models.UserLogin;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {
    private String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.DETECT_SCREEN_CAPTURE,
            Manifest.permission.BLUETOOTH
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.sai.btech.databinding.ActivityWelcomeBinding layoutWidgets = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(layoutWidgets.getRoot());

        layoutWidgets.continueBtn.setOnClickListener(v -> {
            UserLogin login = SharedPreferenceManager.getLoginStatus(this);
            Intent welcomeIntent ;
            if (login.getLoginStatus()){
               welcomeIntent = new Intent(getApplicationContext(), HomeActivity.class);
            }else {
                welcomeIntent = new Intent(getApplicationContext(), LoginActivity.class);
            }startActivity(welcomeIntent);
        });
        checkAndRequestPermission();
    }
    private void checkAndRequestPermission() {
        List<String> notGrantedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                notGrantedPermissions.add(permission);
            }
        }
        if (!notGrantedPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, notGrantedPermissions.toArray(new String[0]), 123);
        }
    }
}