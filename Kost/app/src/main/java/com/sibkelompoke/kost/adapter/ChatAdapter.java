package com.sibkelompoke.kost.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.Chat;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Context context;
    private ArrayList<Chat> chats;
    private String userId;

    public ChatAdapter(Context context, ArrayList<Chat> chats, String userId) {
        this.context = context;
        this.chats = chats;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.message_layout, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        if (userId.equals(chats.get(position).getOutGoingMsgId()) || userId.equals(chats.get(position).getInComingMsgId())) {
            if (userId.equals(chats.get(position).getOutGoingMsgId())) {
                holder.outMsg.setText(chats.get(position).getMessage());
                holder.inMsg.setVisibility(View.GONE);
            } else {
                holder.inMsg.setText(chats.get(position).getMessage());
                holder.inMsg.setVisibility(View.VISIBLE);
                holder.outMsg.setVisibility(View.GONE);
            }
        } else {
            holder.msgNull.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView outMsg, inMsg, msgNull;

        public ChatViewHolder (View view) {
            super(view);
            outMsg = view.findViewById(R.id.outMsg);
            inMsg = view.findViewById(R.id.inMsg);
            msgNull = view.findViewById(R.id.msgNull);
        }
    }
}
