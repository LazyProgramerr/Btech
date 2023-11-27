package com.sai.btech.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sai.btech.R;
import com.sai.btech.adapters.SearchUserAdapter;
import com.sai.btech.AppFeatures.SwipeGesture;
import com.sai.btech.models.UserListModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    SearchUserAdapter adapterUsers;
    DatabaseReference usersReference;
    List<UserListModel> usersList;
    RecyclerView recyclerView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        usersReference = FirebaseDatabase.getInstance().getReference("Users");
        usersReference.keepSynced(true);

        recyclerView = findViewById(R.id.searchUserList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));

        usersList = new ArrayList<>();
        getAllUsers();

        EditText searchUserInput = findViewById(R.id.searchUserInput);
        searchUserInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().trim();
                searchUser(searchText);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        GestureDetector gestureDetector = new GestureDetector(this, new SwipeGesture());
        recyclerView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true; // Consume the event
        });

    }
    private void searchUser(String text) {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserListModel modelUsers = ds.getValue(UserListModel.class);
                    if (modelUsers != null && modelUsers.getuId() != null &&
                            !modelUsers.getuId().equals(currentUser.getUid())) {
                        if (modelUsers.getName().toLowerCase().contains(text.toLowerCase())) {
                            usersList.add(modelUsers);
                        }
                    }
                }
                updateRecyclerView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void getAllUsers() {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserListModel modelUsers = ds.getValue(UserListModel.class);
                    if (modelUsers != null && modelUsers.getuId() != null &&
                            !modelUsers.getuId().equals(currentUser.getUid())) {
                        usersList.add(modelUsers);
                    }
                }
                updateRecyclerView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void updateRecyclerView() {
        // Move the adapter initialization and setting outside the loop
        adapterUsers = new SearchUserAdapter(this, usersList);
        recyclerView.setAdapter(adapterUsers);
    }
}