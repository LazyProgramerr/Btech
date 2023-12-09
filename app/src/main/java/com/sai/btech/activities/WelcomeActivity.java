package com.sai.btech.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sai.btech.R;
import com.sai.btech.authentication.LoginActivity;
import com.sai.btech.models.UserLogin;
import com.sai.btech.sharedPreference.SharedPreferenceManager;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
    public void goToHome(View view){
        UserLogin user = SharedPreferenceManager.getLoginStatus(this);
        if (user.getLoginStatus()) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

    }
}