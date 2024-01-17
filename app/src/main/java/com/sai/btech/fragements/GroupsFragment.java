package com.sai.btech.fragements;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sai.btech.R;
import com.sai.btech.adapters.RecentGroupsAdapter;
import com.sai.btech.models.ChatRoomModel;

import java.util.ArrayList;
import java.util.List;

public class GroupsFragment extends Fragment {
    RecyclerView recyclerView;
    List<ChatRoomModel> groupsModelList = new ArrayList<>();
    RecentGroupsAdapter groupsAdapter;

    public GroupsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View groupsFragment =  inflater.inflate(R.layout.fragment_groups, container, false);

        recyclerView = groupsFragment.findViewById(R.id.recentGroupsRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DatabaseReference groupsReference = FirebaseDatabase.getInstance().getReference("chatRooms").child("groups");
        groupsReference.keepSynced(true);
        groupsAdapter = new RecentGroupsAdapter(getContext(),groupsModelList);
        recyclerView.setAdapter(groupsAdapter);
        groupsReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupsModelList.clear();
                for (DataSnapshot ds:snapshot.getChildren()) {
                    ChatRoomModel chatRoomModel = ds.getValue(ChatRoomModel.class);
                    groupsModelList.add(chatRoomModel);
                }
                groupsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return groupsFragment;
    }
}