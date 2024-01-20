package com.sai.btech.adapters;

import static com.sai.btech.constants.btech.PRIVATE;
import static com.sai.btech.firebase.firebaseData.getData;

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

public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatAdapter.RecentChatsViewHolder> {
    Context context;
    List<ChatRoomModel> chatRoomModelList;

    public RecentChatAdapter(Context context, List<ChatRoomModel> chatRoomModelList) {
        this.context = context;
        this.chatRoomModelList = chatRoomModelList;
    }

    @NonNull
    @Override
    public RecentChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_user,parent,false);
        return new RecentChatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentChatsViewHolder holder, int position) {
        UserData ud = SharedPreferenceManager.getUserData(context);
        ArrayList<String> chatRoomMembers = chatRoomModelList.get(position).getChatRoomMembers();
        String chatRoomImage = chatRoomModelList.get(position).getChatRoomImage();
        String chatRoomId = chatRoomModelList.get(position).getChatRoomId();
        String chatRoomNameId;
        if (chatRoomMembers.contains(ud.getuId())){
            if (chatRoomMembers.get(0).equals(ud.getuId())){
                chatRoomNameId = chatRoomMembers.get(1);
            }else {
                chatRoomNameId = chatRoomMembers.get(0);
            }
            getData(context,chatRoomNameId,ChatRoomName->{
                holder.chatRoomName.setText(ChatRoomName);
                Glide.with(context).load(chatRoomModelList.get(position).getChatRoomImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.chatRoomImg);

                holder.chatRoomName.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("chatRoomImage",chatRoomImage);
                    intent.putExtra("chatRoomName",ChatRoomName);
                    intent.putExtra("chatRoomId",chatRoomId);
                    intent.putExtra("chatRoomType",PRIVATE);
                    intent.putExtra("chatRoomMembers",chatRoomMembers);
                    context.startActivity(intent);
                });
            });
        } else {
            chatRoomNameId = "";
        }


    }

    @Override
    public int getItemCount() {
        return chatRoomModelList.size();
    }

    static class RecentChatsViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView chatRoomImg;
        TextView chatRoomName;
        public RecentChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            chatRoomImg = itemView.findViewById(R.id.miniUserImageView);
            chatRoomName = itemView.findViewById(R.id.recyclerUserName);
        }
    }
}
