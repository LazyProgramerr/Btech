package com.sai.btech.splashScreen;

import static com.sai.btech.AppFeatures.features.changeTheme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.sai.btech.R;
import com.sai.btech.activities.ChatActivity;
import com.sai.btech.activities.HomeActivity;
import com.sai.btech.activities.WelcomeActivity;
import com.sai.btech.models.UserTheme;
import com.sai.btech.sharedPreference.SharedPreferenceManager;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        UserTheme ut = SharedPreferenceManager.getUserTheme(this);
        if (ut.getTheme()) {
            changeTheme(ut.getTheme());
        }
        new Handler().postDelayed(() -> {
            try {
                FirebaseApp.initializeApp(this);
                // Check if the instance has already been initialized with persistence
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                finish();
            } catch (Exception e) {
                startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                finish();
            }
        }, 2000);
    }
}
/*
 if (getIntent().getExtras() != null) {
                // Go to the content
                String receiverName = getIntent().getExtras().getString("userName");
                String receiverImage = getIntent().getExtras().getString("userImg");
                String userId = getIntent().getExtras().getString("userId");
                Intent i = new Intent(this, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);

                Intent intent = new Intent(SplashScreen.this, ChatActivity.class);
                intent.putExtra("receiverUid", userId);
                intent.putExtra("receiverName", receiverName);
                intent.putExtra("receiverImg", receiverImage);
                intent.putExtra("fcmToken", "token[0]");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();


 }try {

        }catch(Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
 */
