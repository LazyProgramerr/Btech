package com.sai.btech.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sai.btech.R;
import com.sai.btech.activities.SearchActivity;

public class ChatsFragment extends Fragment {
    public ChatsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragementChat = inflater.inflate(R.layout.fragment_chats, container, false);
        FloatingActionButton newChat = fragementChat.findViewById(R.id.addNewChart);
        newChat.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), SearchActivity.class));
        });

        return fragementChat;
    }
}