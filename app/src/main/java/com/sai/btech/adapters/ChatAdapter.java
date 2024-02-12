package com.sai.btech.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;
import com.sai.btech.R;
import com.sai.btech.managers.SharedPreferenceManager;
import com.sai.btech.models.ChatMessageModel;
import com.sai.btech.models.UserData;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatModelViewHolder> {
    Context context;
    List<ChatMessageModel> chatMessageModelList;
    String chatRoomType;
    ArrayList<String> selectedChatId = new ArrayList<>();
    boolean isSelectable = false;

    public ChatAdapter(Context context, String chatRoomType, List<ChatMessageModel> chatMessageModelList) {
        this.context = context;
        this.chatMessageModelList = chatMessageModelList;
        this.chatRoomType = chatRoomType;
    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_chat_bubbles,parent,false);
        return new ChatModelViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position) {
        UserData ud = SharedPreferenceManager.getUserData(context);
        String msg = chatMessageModelList.get(position).getMessage();
        String sender = chatMessageModelList.get(position).getSenderId();
        String img = chatMessageModelList.get(position).getSenderImg();
        String id = chatMessageModelList.get(position).getTimestamp();
        if (ud.getuId().equals(sender)){
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            Glide.with(context).load(img).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profileR);
            holder.msgR.setText(msg);
        }else{
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            Glide.with(context).load(img).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profileL);
            holder.msgL.setText(msg);
        }
        holder.leftLayout.setOnLongClickListener(v -> {
            isSelectable = true;
            selectedChatId.add(id);
            holder.chatBubbleHolder.setBackgroundColor(R.color.green);
            return true;
        });holder.rightLayout.setOnLongClickListener(v -> {
            isSelectable = true;
            selectedChatId.add(id);
            holder.chatBubbleHolder.setBackgroundColor(R.color.green);
            return true;
        });
        holder.leftLayout.setOnClickListener(v -> {
            if(isSelectable){
                if(selectedChatId.contains(id)){
                    holder.chatBubbleHolder.setBackgroundColor(Color.TRANSPARENT);
                    selectedChatId.remove(id);
                }else {
                    holder.chatBubbleHolder.setBackgroundColor(R.color.green);
                    selectedChatId.add(id);
                }
                if (selectedChatId.isEmpty()){
                    isSelectable = false;
                }
            }
        });holder.rightLayout.setOnClickListener(v -> {
            if(isSelectable){
                if(selectedChatId.contains(id)){
                    holder.chatBubbleHolder.setBackgroundColor(Color.TRANSPARENT);
                    selectedChatId.remove(id);
                }else {
                    holder.chatBubbleHolder.setBackgroundColor(R.color.green);
                    selectedChatId.add(id);
                }
                if (selectedChatId.isEmpty()){
                    isSelectable = false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatMessageModelList.size();
    }

    static class ChatModelViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView profileL,profileR;
        TextView msgL,msgR;
        RelativeLayout leftLayout,rightLayout,chatBubbleHolder;
        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);
            profileL = itemView.findViewById(R.id.leftProfile);
            profileR = itemView.findViewById(R.id.rightProfile);
            msgL = itemView.findViewById(R.id.msgLeft);
            msgR = itemView.findViewById(R.id.msgRight);
            leftLayout = itemView.findViewById(R.id.leftLayout);
            rightLayout = itemView.findViewById(R.id.rightLayout);
            chatBubbleHolder = itemView.findViewById(R.id.chatBubbleHolder);
        }
    }
}
