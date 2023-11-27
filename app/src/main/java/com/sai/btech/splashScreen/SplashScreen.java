package com.sai.btech.splashScreen;

import static com.sai.btech.AppFeatures.features.changeTheme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.sai.btech.R;
import com.sai.btech.activities.HomeActivity;
import com.sai.btech.models.UserTheme;
import com.sai.btech.sharedPreference.SharedPreferenceManager;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        UserTheme ut = SharedPreferenceManager.getUserTheme(this);
        if (ut.getTheme()){
            changeTheme(ut.getTheme());
        }
        new Handler().postDelayed(()->{
            try {
                FirebaseApp.initializeApp(this);
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            }catch (Exception e){
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            }

        },2000);
    }
}
