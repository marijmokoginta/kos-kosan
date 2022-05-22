package com.sibkelompoke.kost.adapter;

import static com.sibkelompoke.kost.util.KostKonstan.ADMIN;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.Kost;
import com.sibkelompoke.kost.model.User;

import java.util.ArrayList;

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.ListChatViewHolder> {
    private Context context;
    private ArrayList<User> users;

    private OnChatClickListener onChatClickListener;

    public ListChatAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ListChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_chat, parent, false);
        return new ListChatViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ListChatViewHolder holder, int position) {
        holder.username.setText(users.get(position).getUsername());
        if (users.get(position).getRole().equals(ADMIN)) {
            holder.userInfo.setText("Pemilik kost");
        } else {
            holder.userInfo.setText("Teman Kost");
        }
        Glide.with(context).load(users.get(position).getImageUrl()).into(holder.photoProfile);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface OnChatClickListener {
        void onChatClick (View view, User user);
    }

    public void setOnChatClickListener(OnChatClickListener onChatClickListener) {
        this.onChatClickListener = onChatClickListener;
    }

    public class ListChatViewHolder extends RecyclerView.ViewHolder {
        ImageView photoProfile;
        TextView username, userInfo, unreadChat;
        CardView unreadChatWrap;

        public ListChatViewHolder(@NonNull View itemView) {
            super(itemView);

            photoProfile = itemView.findViewById(R.id.photoProfile);
            username = itemView.findViewById(R.id.username);
            userInfo = itemView.findViewById(R.id.userInfo);
            unreadChat = itemView.findViewById(R.id.unReadChat);
            unreadChatWrap = itemView.findViewById(R.id.unReadChatWrap);

            itemView.setOnClickListener(v -> {
                if (onChatClickListener != null) onChatClickListener.onChatClick(itemView, users.get(getAdapterPosition()));
            });
        }
    }
}
