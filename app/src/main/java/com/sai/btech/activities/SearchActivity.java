package com.sai.btech.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sai.btech.R;
import com.sai.btech.adapters.SearchUserAdapter;
import com.sai.btech.models.UserListModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private SearchUserAdapter searchUserAdapter;
    private DatabaseReference usersReference;
    private List<UserListModel> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        usersReference = FirebaseDatabase.getInstance().getReference("Users");
        usersReference.keepSynced(true);

        Uri sharedImageUri = null;
        Intent receivedIntent = getIntent();
        String receiverAction = receivedIntent.getAction();
        if (receiverAction != null && receiverAction.equals(Intent.ACTION_SEND)){
            if (receivedIntent.hasExtra(Intent.EXTRA_STREAM)){
                sharedImageUri = (Uri) receivedIntent.getParcelableExtra(Intent.EXTRA_STREAM);
            }
        }

        RecyclerView recyclerView = findViewById(R.id.searchUserRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersList = new ArrayList<>();
        searchUserAdapter = new SearchUserAdapter(this, usersList,sharedImageUri);
        recyclerView.setAdapter(searchUserAdapter);
        getAllUsers();

    }
    private void getAllUsers() {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        usersReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserListModel modelUsers = ds.getValue(UserListModel.class);
                    if (modelUsers != null && modelUsers.getuId() != null) {
                        assert currentUser != null;
                        if (!modelUsers.getuId().equals(currentUser.getUid())) {
                            usersList.add(modelUsers);
                        }
                    }
                }searchUserAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}