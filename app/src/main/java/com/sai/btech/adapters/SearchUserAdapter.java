package com.sai.btech.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;
import com.sai.btech.R;
import com.sai.btech.activities.ChatActivity;
import com.sai.btech.managers.SharedPreferenceManager;
import com.sai.btech.models.UserListModel;

import java.util.ArrayList;
import java.util.List;

import static com.sai.btech.constants.btech.PRIVATE;
import static com.sai.btech.constants.btech.VIEW_TYPE_HEADER;
import static com.sai.btech.constants.btech.VIEW_TYPE_ITEM;
import static com.sai.btech.managers.ChatRoomManager.ChatRoomId;

public class SearchUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @SuppressLint("StaticFieldLeak")
    static Context context;
    List<UserListModel> usersList;
    static Uri sharedImage;
    public SearchUserAdapter(Context context, List<UserListModel> usersList,@Nullable Uri SharedImg) {
        SearchUserAdapter.context = context;
        this.usersList = usersList;
        sharedImage = SharedImg;
    }
    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View headerView = LayoutInflater.from(context).inflate(R.layout.layout_user, parent, false);
            return new SearchHeaderViewHolder(headerView);
        } else {
            View itemView = LayoutInflater.from(context).inflate(R.layout.layout_user, parent, false);
            return new UserModelViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserModelViewHolder) {
            UserListModel user = usersList.get(position - 1); // Adjust position for header
            ((UserModelViewHolder) holder).bindUser(user);
        }
    }

    @Override
    public int getItemCount() {
        return usersList.size() + 1; // Add 1 for the header
    }

    static class UserModelViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        ShapeableImageView profilePic;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.recyclerUserName);
            profilePic = itemView.findViewById(R.id.miniUserImageView);
        }
        public void bindUser(UserListModel user) {
            // Your existing user binding logic
            userName.setText(user.getName());
            try {
                Glide.with(context).load(user.getUserImg()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_user_icon).into(profilePic);
            } catch(Exception err) {
                Glide.with(context).load(R.drawable.default_user_icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(profilePic);
            }

            ArrayList<String> chatRoomMembers = new ArrayList<>();
            chatRoomMembers.add(user.getuId());
            chatRoomMembers.add(SharedPreferenceManager.getUserData(context).getuId());

            userName.setOnClickListener(v -> {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("chatRoomImage", user.getUserImg());
                intent.putExtra("chatRoomName", user.getName());
                intent.putExtra("chatRoomId", ChatRoomId(SharedPreferenceManager.getUserData(context).getuId(), user.getuId()));
                intent.putExtra("chatRoomType", PRIVATE);
                intent.putStringArrayListExtra("chatRoomMembers", chatRoomMembers);
                intent.putExtra("sharedImage",sharedImage);
                context.startActivity(intent);
            });
        }
    }

    static class SearchHeaderViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView icon;
        TextView text;
        public SearchHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.recyclerUserName);
            icon = itemView.findViewById(R.id.miniUserImageView);

            text.setText(R.string.new_group);
            Glide.with(context).load(R.drawable.group_icon).into(icon);

            itemView.setOnClickListener(v -> Toast.makeText(context, R.string.new_group, Toast.LENGTH_SHORT).show());

        }

    }
}
