package com.sai.btech.adapters;

import static com.sai.btech.constants.btech.PRIVATE;

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
import com.sai.btech.models.ChatRoomModel;
import com.sai.btech.models.UserData;

import java.util.ArrayList;
import java.util.List;

public class RecentGroupsAdapter extends RecyclerView.Adapter<RecentGroupsAdapter.GroupViewsHolder> {
    Context context;
    List<ChatRoomModel> chatRoomModelList;

    public RecentGroupsAdapter(Context context, List<ChatRoomModel> chatRoomModelList) {
        this.context = context;
        this.chatRoomModelList = chatRoomModelList;
    }

    @NonNull
    @Override
    public GroupViewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_user,parent,false);
        return new GroupViewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewsHolder holder, int position) {
        UserData ud = SharedPreferenceManager.getUserData(context);
        String chatRoomName = chatRoomModelList.get(position).getChatRoomName();
        String chatRoomImage = chatRoomModelList.get(position).getChatRoomImage();
        ArrayList<String> chatRoomMembers = chatRoomModelList.get(position).getChatRoomMembers();
        String chatRoomId = chatRoomModelList.get(position).getChatRoomId();
        if (chatRoomMembers.contains(ud.getuId())){
            holder.chatRoomName.setText(chatRoomName);
            Glide.with(context).load(chatRoomImage).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.chatRoomImage);

            holder.chatRoomName.setOnClickListener(v -> {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("chatRoomImage",chatRoomImage);
                intent.putExtra("chatRoomName",chatRoomName);
                intent.putExtra("chatRoomId",chatRoomId);
                intent.putExtra("chatRoomType","groups");
                intent.putExtra("chatRoomMembers",chatRoomMembers);
                context.startActivity(intent);
            });
        }

    }

    @Override
    public int getItemCount() {
        return chatRoomModelList.size();
    }

    static class GroupViewsHolder extends RecyclerView.ViewHolder{
        ShapeableImageView chatRoomImage;
        TextView chatRoomName;
        public GroupViewsHolder(@NonNull View itemView) {
            super(itemView);
            chatRoomImage = itemView.findViewById(R.id.miniUserImageView);
            chatRoomName = itemView.findViewById(R.id.recyclerUserName);
        }
    }
}
