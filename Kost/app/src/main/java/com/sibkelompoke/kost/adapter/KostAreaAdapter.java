package com.sibkelompoke.kost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.KostArea;

import java.util.ArrayList;

public class KostAreaAdapter extends RecyclerView.Adapter<KostAreaAdapter.AreaViewHolder> {
    private OnItemClickListener onItemClickListener;

    private Context context;
    private ArrayList<KostArea> areas;

    public KostAreaAdapter(Context context, ArrayList<KostArea> areas) {
        this.context = context;
        this.areas = areas;
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.view_layout_area, parent, false);
        return new AreaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        holder.image.setImageResource(areas.get(position).getImage());
        holder.text.setText(areas.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return areas.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, KostArea kostArea);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView text;
        public AreaViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.imgHighlight);
            text = v.findViewById(R.id.textArea);
            v.setOnClickListener(view -> {
                if (onItemClickListener != null) onItemClickListener.onItemClick(view, areas.get(getAdapterPosition()));
            });
        }
    }
}
