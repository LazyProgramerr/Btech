package com.sai.btech.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sai.btech.MainActivity;
import com.sai.btech.R;
import com.sai.btech.authentication.LoginActivity;
import com.sai.btech.databinding.ActivityWelcomeBinding;
import com.sai.btech.managers.SharedPreferenceManager;
import com.sai.btech.models.UserData;
import com.sai.btech.models.UserLogin;

public class WelcomeActivity extends AppCompatActivity {
    private ActivityWelcomeBinding layoutWidgets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutWidgets = ActivityWelcomeBinding.inflate(getLayoutInflater());
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
    }
}