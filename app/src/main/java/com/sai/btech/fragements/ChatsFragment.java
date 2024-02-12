package com.sai.btech.fragements;

import static com.sai.btech.constants.btech.PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sai.btech.R;
import com.sai.btech.activities.SearchActivity;
import com.sai.btech.adapters.RecentChatAdapter;
import com.sai.btech.managers.SharedPreferenceManager;
import com.sai.btech.models.ChatRoomModel;
import com.sai.btech.models.UserData;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {
    private DatabaseReference chatsReference;
    RecentChatAdapter recentChatAdapter;
    private List<ChatRoomModel> chatRoomModelList;
    private UserData ud;

    public ChatsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View chatsFragment = inflater.inflate(R.layout.fragment_chats, container, false);
        FloatingActionButton fab = chatsFragment.findViewById(R.id.newChat);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            startActivity(intent);
        });
        chatsReference = FirebaseDatabase.getInstance().getReference("chatRooms").child(PRIVATE);
        chatsReference.keepSynced(true);
        RecyclerView recyclerView = chatsFragment.findViewById(R.id.chatRoomsRecyclerView);
        chatRoomModelList = new ArrayList<>();
        ud = SharedPreferenceManager.getUserData(requireContext());
        fetchChatRooms(recyclerView);

        return chatsFragment;
    }

    private void fetchChatRooms(RecyclerView recyclerView) {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recentChatAdapter = new RecentChatAdapter(getContext(),chatRoomModelList);
        recyclerView.setAdapter(recentChatAdapter);
        chatsReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatRoomModelList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatRoomModel chatRoomModel = dataSnapshot.getValue(ChatRoomModel.class);
                    assert chatRoomModel != null;
                    if (chatRoomModel.getChatRoomMembers().contains(ud.getuId())){
                        chatRoomModelList.add(chatRoomModel);
                    }
                }
                recentChatAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }
}