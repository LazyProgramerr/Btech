package com.sai.btech.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        @Nullable
        String msgType = chatMessageModelList.get(position).getMsgType();
        if (ud.getuId().equals(sender)){
            if (msgType == null || msgType.equals("text")){
                holder.leftLayout.setVisibility(View.GONE);
                holder.rightLayout.setVisibility(View.VISIBLE);
                holder.rightImageLayout.setVisibility(View.GONE);
                holder.leftImageLayout.setVisibility(View.GONE);
                Glide.with(context).load(img).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profileR);
                holder.msgR.setText(msg);
            }else if(msgType.equals("image")){
                holder.rightImageLayout.setVisibility(View.VISIBLE);
                holder.leftImageLayout.setVisibility(View.GONE);
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftLayout.setVisibility(View.GONE);
                Glide.with(context).load(img).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profileRImg);
                Glide.with(context).load(msg).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_user_icon).into(holder.rightImage);

            }
        }else{
            if (msgType == null || msgType.equals("text")){
                holder.leftLayout.setVisibility(View.VISIBLE);
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftImageLayout.setVisibility(View.GONE);
                holder.rightImageLayout.setVisibility(View.GONE);
                Glide.with(context).load(img).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profileL);
                holder.msgL.setText(msg);
            }else if (msgType.equals("image")){
                holder.rightImageLayout.setVisibility(View.GONE);
                holder.leftImageLayout.setVisibility(View.VISIBLE);
                holder.rightLayout.setVisibility(View.GONE);
                holder.leftLayout.setVisibility(View.GONE);
                Glide.with(context).load(img).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profileLImg);
                Glide.with(context).load(msg).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_user_icon).into(holder.leftImage);
            }
        }
//        holder.leftLayout.setOnLongClickListener(v -> {
//            isSelectable = true;
//            selectedChatId.add(id);
//            holder.chatBubbleHolder.setBackgroundColor(R.color.green);
//            return true;
//        });holder.rightLayout.setOnLongClickListener(v -> {
//            isSelectable = true;
//            selectedChatId.add(id);
//            holder.chatBubbleHolder.setBackgroundColor(R.color.green);
//            return true;
//        });
//        holder.leftLayout.setOnClickListener(v -> {
//            if(isSelectable){
//                if(selectedChatId.contains(id)){
//                    holder.chatBubbleHolder.setBackgroundColor(Color.TRANSPARENT);
//                    selectedChatId.remove(id);
//                }else {
//                    holder.chatBubbleHolder.setBackgroundColor(R.color.green);
//                    selectedChatId.add(id);
//                }
//                if (selectedChatId.isEmpty()){
//                    isSelectable = false;
//                }
//            }
//        });holder.rightLayout.setOnClickListener(v -> {
//            if(isSelectable){
//                if(selectedChatId.contains(id)){
//                    holder.chatBubbleHolder.setBackgroundColor(Color.TRANSPARENT);
//                    selectedChatId.remove(id);
//                }else {
//                    holder.chatBubbleHolder.setBackgroundColor(R.color.green);
//                    selectedChatId.add(id);
//                }
//                if (selectedChatId.isEmpty()){
//                    isSelectable = false;
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return chatMessageModelList.size();
    }

    static class ChatModelViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView profileL,profileR,profileLImg,profileRImg;
        TextView msgL,msgR;
        LinearLayout leftImageLayout,rightImageLayout;
        ImageView leftImage,rightImage;

        RelativeLayout leftLayout,rightLayout,chatBubbleHolder;
        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);
            profileL = itemView.findViewById(R.id.leftProfile);
            profileR = itemView.findViewById(R.id.rightProfile);
            profileLImg = itemView.findViewById(R.id.leftProfile1);
            profileRImg = itemView.findViewById(R.id.rightProfile1);
            msgL = itemView.findViewById(R.id.msgLeft);
            msgR = itemView.findViewById(R.id.msgRight);
            leftLayout = itemView.findViewById(R.id.leftLayout);
            rightLayout = itemView.findViewById(R.id.rightLayout);
            chatBubbleHolder = itemView.findViewById(R.id.chatBubbleHolder);
            leftImageLayout = itemView.findViewById(R.id.leftImageLayout);
            rightImageLayout = itemView.findViewById(R.id.rightImageLayout);
            leftImage = itemView.findViewById(R.id.leftImage);
            rightImage = itemView.findViewById(R.id.rightImage);
        }
    }
}
