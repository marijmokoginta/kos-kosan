package com.sibkelompoke.kost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sibkelompoke.kost.R;

import java.util.ArrayList;

public class PengaturanAdapter extends RecyclerView.Adapter<PengaturanAdapter.PengaturanViewHolder> {
    private final Context context;
    private final String[] pengaturan;

    private OnItemClickListener onItemClickListener;

    public PengaturanAdapter(Context context, String[] pengaturan) {
        this.context = context;
        this.pengaturan = pengaturan;
    }

    @NonNull
    @Override
    public PengaturanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_pengaturan, parent, false);
        return new PengaturanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PengaturanViewHolder holder, int position) {
        holder.nmPengaturan.setText(pengaturan[position]);
    }

    @Override
    public int getItemCount() {
        return pengaturan.length;
    }

    public interface OnItemClickListener{
        void onItemClick(String nmPengaturan);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class PengaturanViewHolder extends RecyclerView.ViewHolder {
        TextView nmPengaturan;
        public PengaturanViewHolder(@NonNull View itemView) {
            super(itemView);
            nmPengaturan = itemView.findViewById(R.id.nmPengaturan);

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(pengaturan[getAdapterPosition()]);
                }
            });
        }
    }
}
