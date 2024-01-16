package com.sai.btech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.sai.btech.activities.WelcomeActivity;
import com.sai.btech.databinding.ActivityMainBinding;
import com.sai.btech.managers.SharedPreferenceManager;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.sai.btech.databinding.ActivityMainBinding layoutWidgets = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(layoutWidgets.getRoot());
        new Handler().postDelayed(()->{
            try {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            }catch (Exception e){
                Log.e("persistence ","it is already enabled");
            }
            startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
            finish();
        },2000);

    }
}