package com.sai.btech.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.sai.btech.R;
import com.sai.btech.activities.SearchActivity;
import com.sai.btech.adapters.RecentChatAdapter;
import com.sai.btech.models.ChatRoomModel;
import com.sai.btech.models.UserData;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {
    public ChatsFragment() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    RecentChatAdapter recentChatAdapter;
    List<ChatRoomModel> recentList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragementChat = inflater.inflate(R.layout.fragment_chats, container, false);
        FloatingActionButton newChat = fragementChat.findViewById(R.id.addNewChart);
        newChat.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), SearchActivity.class));
        });

        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("chatRooms/private");
        usersReference.keepSynced(true);

        recyclerView = fragementChat.findViewById(R.id.recentChats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recentList = new ArrayList<>();
        recentChatAdapter = new RecentChatAdapter(getContext(),recentList);
        recyclerView.setAdapter(recentChatAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        usersReference.child("private").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing list before adding new data
                recentList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatRoomModel chatRoomModel = dataSnapshot.getValue(ChatRoomModel.class);
                    assert chatRoomModel != null;
                    if (chatRoomModel.getChatRoomMembers().contains(user.getUid()))
                        recentList.add(chatRoomModel);
                }
                // Notify the adapter that the data has changed
                    recentChatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error, if any
            }
        });

        return fragementChat;
    }
}