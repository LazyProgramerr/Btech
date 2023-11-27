package com.sai.btech.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.FragmentManager;

import com.sai.btech.R;
import com.sai.btech.databinding.ActivityHomeBinding;
import com.sai.btech.AppFeatures.SwipeGesture;
import com.sai.btech.fragments.ChatsFragment;
import com.sai.btech.fragments.GroupsFragment;
import com.sai.btech.fragments.HomeFragment;
import com.sai.btech.fragments.ProfileFragment;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class HomeActivity extends AppCompatActivity {
    private static FragmentManager fragmentManager;
    private GestureDetectorCompat gestureDetector;
    public static int fragementPosition = 0;
    public static SmoothBottomBar smoothBottomBar;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.sai.btech.databinding.ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        smoothBottomBar = findViewById(R.id.bottomBar);
        fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.display, homeFragment).commit();

        smoothBottomBar.setOnItemSelectedListener((OnItemSelectedListener) itemIndex -> {
            fragementPosition = itemIndex;
            change(itemIndex);
            return true;
        });
        gestureDetector = new GestureDetectorCompat(this, new SwipeGesture());

        // Attach a touch listener to your main content layout
        FrameLayout mainContent = findViewById(R.id.display);
        mainContent.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true; // Consume the event
        });
//        Bio.BioAuth(this);



    }
    public static void change(int i){
        switch (i) {
            case 0 -> {
                HomeFragment homeFragment1 = new HomeFragment();
                fragmentManager.beginTransaction().replace(R.id.display, homeFragment1).commit();
            }
            case 1 -> {
                ChatsFragment chatsFragment = new ChatsFragment();
                fragmentManager.beginTransaction().replace(R.id.display, chatsFragment).commit();
            }
            case 2 -> {
                GroupsFragment groupsFragment = new GroupsFragment();
                fragmentManager.beginTransaction().replace(R.id.display, groupsFragment).commit();
            }
            case 3 ->{
                ProfileFragment profileFragment = new ProfileFragment();
                fragmentManager.beginTransaction().replace(R.id.display,profileFragment).commit();
            }
        }
    }




}