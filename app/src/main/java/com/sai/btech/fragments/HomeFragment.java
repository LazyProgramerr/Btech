package com.sai.btech.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.sai.btech.R;
//import com.sai.btech.Dialogs.AlertDialog;
import com.sai.btech.models.AppStatus;
import com.sai.btech.sharedPreference.SharedPreferenceManager;
import com.sai.btech.activities.SettingsActivity;
import com.sai.btech.databinding.FragmentHomeBinding;

import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    // Remove the binding variable

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout directly without using View Binding
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                AppStatus as = SharedPreferenceManager.getAppStatus(requireContext());
                if (as.getAppStatus()) {
                    ActivityManager am = (ActivityManager) requireContext().getSystemService(Context.ACTIVITY_SERVICE);
                    if (am != null) {
                        List<ActivityManager.AppTask> tasks = am.getAppTasks();
                        if (tasks != null && !tasks.isEmpty()) {
                            String currentPackageName = requireContext().getPackageName();
                            for (ActivityManager.AppTask task : tasks) {
                                ActivityManager.RecentTaskInfo taskInfo = task.getTaskInfo();
                                if (currentPackageName.equals(Objects.requireNonNull(taskInfo.baseIntent.getComponent()).getPackageName())) {
                                    task.setExcludeFromRecents(true);
                                    break;
                                }
                            }
                        }
                    }
                }
                requireActivity().finishAffinity();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        // Remove references to binding and replace with direct references to views
        View moreButton = view.findViewById(R.id.more);
        moreButton.setOnClickListener(this::show);
    }

    private void show(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        requireActivity().getMenuInflater().inflate(R.menu.more_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.logout) {
                SharedPreferenceManager.saveLoginStatus(requireContext(), false);
                FirebaseAuth.getInstance().signOut();
//                AlertDialog alertDialog = new AlertDialog(requireActivity(),"qwerty");
//                alertDialog.show();
                // startActivity(new Intent(requireContext(), WelcomeActivity.class));
                return true;
            } else if (itemId == R.id.settings) {
                startActivity(new Intent(requireContext(), SettingsActivity.class));
                return true;
            }
            return false;
        });

        popupMenu.show();
    }
}
