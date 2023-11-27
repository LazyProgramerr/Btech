package com.sai.btech.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;
import com.sai.btech.R;
import com.sai.btech.activities.ChatActivity;
import com.sai.btech.dialogs.ProfileDialog;
import com.sai.btech.models.UserListModel;

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
        View view = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false);
        return new UserModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserAdapter.UserModelViewHolder holder, int position) {
        String receiverUid = usersList.get(position).getuId();
        String userImage = usersList.get(position).getImage();
        String userName = usersList.get(position).getName();

        UserModelViewHolder.uName.setText(userName);
        try {
            Glide.with(context).load(userImage).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.default_user_icon).into(UserModelViewHolder.profilePic);
        } catch(Exception err) {
            Glide.with(context).load(R.drawable.default_user_icon).diskCacheStrategy(DiskCacheStrategy.ALL).into(UserModelViewHolder.profilePic);
        }

        UserModelViewHolder.uName.setOnClickListener(v -> {
//            Toast.makeText(context, ""+userName, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("receiverUid", receiverUid);
            intent.putExtra("receiverName", userName);
            intent.putExtra("receiverImg", userImage);
            context.startActivity(intent);
        });
        UserModelViewHolder.profilePic.setOnClickListener(v->{
            ProfileDialog profileDialog = new ProfileDialog(v.getContext(), userImage);
            profileDialog.setCanceledOnTouchOutside(true);
            profileDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
    static class UserModelViewHolder extends RecyclerView.ViewHolder{
        @SuppressLint("StaticFieldLeak")
        static TextView uName;
        static ShapeableImageView profilePic;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            uName = itemView.findViewById(R.id.userName);
            profilePic = itemView.findViewById(R.id.profilePicView);

        }
    }
}
