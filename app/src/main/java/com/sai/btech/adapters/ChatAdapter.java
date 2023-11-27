package com.sai.btech.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sai.btech.R;
import com.sai.btech.models.ChatMessageModel;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    Context context;
    ArrayList<ChatMessageModel> chatMessageModelArrayList;

    public ChatAdapter(Context context, ArrayList<ChatMessageModel> chatMessageModelArrayList) {
        this.context = context;
        this.chatMessageModelArrayList = chatMessageModelArrayList;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.message_layout,parent,false);
        return new ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        ChatMessageModel chatMessageModel = chatMessageModelArrayList.get(position);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (chatMessageModel.getSenderId().equals(user.getUid())){
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightChat.setText(chatMessageModel.getMessage());
            holder.rightTime.setText(chatMessageModel.getTimestamp());
        }else{
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftChat.setText(chatMessageModel.getMessage());
            holder.leftTime.setText(chatMessageModel.getTimestamp());
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageModelArrayList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView leftChat,rightChat,leftTime,rightTime;
        LinearLayout leftLayout,rightLayout;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            rightChat = itemView.findViewById(R.id.chat_user_message);
            leftTime = itemView.findViewById(R.id.receive_chat_message_time);
            rightTime = itemView.findViewById(R.id.sent_chat_message_time);
            leftLayout = itemView.findViewById(R.id.leftMsgLayout);
            rightLayout = itemView.findViewById(R.id.rightMsgLayout);
            leftChat = itemView.findViewById(R.id.chat_receiver_message);


        }
    }
}
