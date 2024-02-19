package com.sai.btech.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.sai.btech.R;
import com.sai.btech.databinding.ActivityHomeBinding;
import com.sai.btech.fragements.ChatsFragment;
import com.sai.btech.fragements.GroupsFragment;
import com.sai.btech.fragements.HomeFragment;
import com.sai.btech.fragements.UserFragment;
import com.sai.btech.managers.SharedPreferenceManager;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding layoutWidgets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutWidgets = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(layoutWidgets.getRoot());

        FragmentManager fragmentManager = getSupportFragmentManager();

        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.fragmentsContainer,homeFragment).commit();
        layoutWidgets.bottomNavigationBar.setOnItemSelected(integer -> {
            switch (integer) {
                case 0 -> {
                    HomeFragment homeFragment1 = new HomeFragment();
                    fragmentManager.beginTransaction().replace(R.id.fragmentsContainer, homeFragment1).commitAllowingStateLoss();
                    layoutWidgets.topPanelText.setText(R.string.app_name);
                }
                case 1 -> {
                    ChatsFragment chatsFragment = new ChatsFragment();
                    fragmentManager.beginTransaction().replace(R.id.fragmentsContainer, chatsFragment).commitAllowingStateLoss();
                    layoutWidgets.topPanelText.setText(R.string.chats);
                }
                case 2 -> {
                    GroupsFragment groupsFragment = new GroupsFragment();
                    fragmentManager.beginTransaction().replace(R.id.fragmentsContainer, groupsFragment).commitAllowingStateLoss();
                    layoutWidgets.topPanelText.setText(R.string.groups);
                }
                case 3 ->{
                    UserFragment userFragment = new UserFragment();
                    fragmentManager.beginTransaction().replace(R.id.fragmentsContainer,userFragment).commit();
                    layoutWidgets.topPanelText.setText(R.string.profile);
                }
            }
            return null;
        });
        layoutWidgets.topPanelOptions.setOnClickListener(v -> show(layoutWidgets.topPanelOptions));
    }
    private void show(View view) {
        PopupMenu popupMenu = new PopupMenu(this,view);

        getMenuInflater().inflate(R.menu.more_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.logout) {
                SharedPreferenceManager.saveLoginStatus(this, false);
                FirebaseAuth.getInstance().signOut();
//                AlertDialog alertDialog = new AlertDialog(requireActivity(),"qwerty");
//                alertDialog.show();
                startActivity(new Intent(this, WelcomeActivity.class));
                return true;
            } else if (itemId == R.id.settings) {
//                startActivity(new Intent(this, SettingsActivity.class));
                Snackbar.make(view,"clicked on settings",Snackbar.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        popupMenu.show();
    }
    public static void showMessage(String msg){

    }
}