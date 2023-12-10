package com.sai.btech.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sai.btech.AppFeatures.features;
import com.sai.btech.R;
import com.sai.btech.activities.ChatActivity;
import com.sai.btech.dialogs.ProfileDialog;
import com.sai.btech.models.ChatRoomModel;

import java.util.List;

public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatAdapter.UserChatModelViewHolder> {
    Context context;
    List<ChatRoomModel> usersList;
    FirebaseUser user;



    public RecentChatAdapter(Context context, List<ChatRoomModel> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public RecentChatAdapter.UserChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_layout,parent,false);
        return new UserChatModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentChatAdapter.UserChatModelViewHolder holder, int position) {
        String user1,user2,receiverUid,msg,lastMsgSender,lastMessageTime;;
        user1 = usersList.get(position).getUser1();
        user2 = usersList.get(position).getUser2();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user1.equals(user.getUid())){
            receiverUid = user2;
            setDetails(holder,receiverUid);
        } else {
            receiverUid = user1;
            setDetails(holder,receiverUid);
        }
        lastMsgSender = usersList.get(position).getLastMsgSenderId();
        lastMessageTime = usersList.get(position).getMsgTimeStamp();
        holder.time.setText(features.readableTime(lastMessageTime));
        msg = usersList.get(position).getMsg();
        if (msg.length() > 20) {
            int lengthToReplace = msg.length() - 20;
            String maskedText = msg.substring(lengthToReplace) + "....";
            msg = maskedText;
        }
        if(lastMsgSender.equals(user.getUid()))
            holder.lastChat.setText("");
        else
            holder.lastChat.setText(msg);


    }
    private void setDetails(UserChatModelViewHolder holder,String receiverUid){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
        Query query = db.orderByChild("uId").equalTo(receiverUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String receiverName = "" + ds.child("name").getValue();
                    String receiverImage = "" + ds.child("image").getValue();

                    holder.uName.setText(receiverName);
                    Glide.with(context)
                            .load(receiverImage)
                            .placeholder(R.drawable.default_user_icon)
                            .into(holder.profilePic);
                    holder.profilePic.setOnClickListener(v -> {
                        ProfileDialog profileDialog = new ProfileDialog(context,receiverImage);
                        profileDialog.setCancelable(true);
                        profileDialog.show();
                    });

                    holder.layout.setOnClickListener(v -> {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("receiverUid", receiverUid);
                        intent.putExtra("receiverName", receiverName);
                        intent.putExtra("receiverImg", receiverImage);
                        intent.putExtra("fcmToken","jbhug");
                        context.startActivity(intent);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError arg0) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
    public static class UserChatModelViewHolder extends RecyclerView.ViewHolder{
        @SuppressLint("StaticFieldLeak")
        TextView uName,time,lastChat;
        ShapeableImageView profilePic;
        LinearLayout layout;

        public UserChatModelViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.detailsLayout);
            uName = itemView.findViewById(R.id.recentChatsUserName);
            profilePic = itemView.findViewById(R.id.profilePicView);
            time = itemView.findViewById(R.id.lastMsgTime);
            lastChat = itemView.findViewById(R.id.lastChat);
        }

    }


}

