package com.sai.btech.adapters;

import static com.sai.btech.constants.btech.PRIVATE;
import static com.sai.btech.managers.ChatRoomManager.ChatRoomId;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;
import com.sai.btech.R;
import com.sai.btech.activities.ChatActivity;
import com.sai.btech.managers.SharedPreferenceManager;
import com.sai.btech.models.UserData;
import com.sai.btech.models.UserListModel;

import java.util.ArrayList;
import java.util.List;


public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.UserModelViewHolder> {
    Context context;
    List<UserListModel> usersList;

    public SearchUserAdapter(Context context, List<UserListModel> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public SearchUserAdapter.UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_user,parent,false);
        return new UserModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserAdapter.UserModelViewHolder holder, int position) {
        String receiverUid = usersList.get(position).getuId();
        String receiverImage = usersList.get(position).getUserImg();
        String receiverName = usersList.get(position).getName();
        UserData ud = SharedPreferenceManager.getUserData(context);

        UserModelViewHolder.userName.setText(receiverName);
        try {
            Glide.with(context).load(receiverImage).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_user_icon).into(UserModelViewHolder.profilePic);
        } catch(Exception err) {
            Glide.with(context).load(R.drawable.default_user_icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(UserModelViewHolder.profilePic);
        }
        ArrayList<String> chatRoomMembers = new ArrayList<>();
        chatRoomMembers.add(receiverUid);
        chatRoomMembers.add(ud.getuId());

        UserModelViewHolder.userName.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("chatRoomImage",receiverImage);
            intent.putExtra("chatRoomName",receiverName);
            intent.putExtra("chatRoomId",ChatRoomId(ud.getuId(),receiverUid));
            intent.putExtra("chatRoomType",PRIVATE);
            intent.putStringArrayListExtra("chatRoomMembers",chatRoomMembers);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
    static class UserModelViewHolder extends RecyclerView.ViewHolder{
        @SuppressLint("StaticFieldLeak")
        static TextView userName;
        static ShapeableImageView profilePic;
        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.recyclerUserName);
            profilePic = itemView.findViewById(R.id.miniUserImageView);
        }
    }
}
