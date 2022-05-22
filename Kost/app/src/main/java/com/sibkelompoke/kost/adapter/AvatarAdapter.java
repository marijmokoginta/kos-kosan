package com.sibkelompoke.kost.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sibkelompoke.kost.R;

import java.util.ArrayList;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder> {
    private final Context context;
    private final ArrayList<Uri> avatarUrls;

    private OnAvatarClickListener onAvatarClickListener;

    public AvatarAdapter(Context context, ArrayList<Uri> avatarUrls) {
        this.context = context;
        this.avatarUrls = avatarUrls;
    }

    @NonNull
    @Override
    public AvatarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_avatar, parent, false);
        return new AvatarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarViewHolder holder, int position) {
        Glide.with(context).load(avatarUrls.get(position)).into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return avatarUrls.size();
    }

    public interface OnAvatarClickListener{
        void onAvatarClick(Uri uri);
    }

    public void setOnAvatarClickListener(OnAvatarClickListener onAvatarClickListener) {
        this.onAvatarClickListener = onAvatarClickListener;
    }

    public class AvatarViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        public AvatarViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);

            itemView.setOnClickListener(v -> {
                if (onAvatarClickListener != null)
                    onAvatarClickListener.onAvatarClick(avatarUrls.get(getAdapterPosition()));
            });
        }
    }
}
