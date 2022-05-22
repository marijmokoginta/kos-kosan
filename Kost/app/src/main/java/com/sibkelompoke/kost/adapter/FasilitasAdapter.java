package com.sibkelompoke.kost.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.FasilitasKamar;

import java.util.ArrayList;

public class FasilitasAdapter extends RecyclerView.Adapter<FasilitasAdapter.FasilitasViewHolder> {
    private final Context context;
    private final ArrayList<FasilitasKamar> fasilitas;

    private OnItemClickListener onItemClickListener;

    public FasilitasAdapter(Context context, ArrayList<FasilitasKamar> fasilitas) {
        this.context = context;
        this.fasilitas = fasilitas;
    }

    @NonNull
    @Override
    public FasilitasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fasilitas_kamar, parent, false);
        return new FasilitasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FasilitasViewHolder holder, int position) {
        holder.fasilitasName.setText(fasilitas.get(position).getFasilitasName());
        Glide.with(context).load(fasilitas.get(position).getFasilitasImg()).into(holder.fasilitasImg);
    }

    @Override
    public int getItemCount() {
        return fasilitas.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ArrayList<FasilitasKamar> fasilitasKamar);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class FasilitasViewHolder extends RecyclerView.ViewHolder {
        ImageView fasilitasImg, check;
        TextView fasilitasName;
        LinearLayout boxItem;

        public FasilitasViewHolder(View view) {
            super(view);

            fasilitasImg = view.findViewById(R.id.fasilitasImg);
            fasilitasName = view.findViewById(R.id.fasilitasName);
            check = view.findViewById(R.id.check);
            boxItem = view.findViewById(R.id.boxItem);

            ArrayList<FasilitasKamar> fasilitasKamars = new ArrayList<>();

            view.setOnClickListener(v -> {
                if (check.getVisibility() == View.GONE) {
                    fasilitasKamars.add(fasilitas.get(getAdapterPosition()));
                    check.setVisibility(View.VISIBLE);
                    boxItem.setBackgroundColor(Color.rgb(198, 219, 255));
                } else if (check.getVisibility() == View.VISIBLE) {
                    for (int i = 0; i < fasilitasKamars.size(); i++) {
                        if (fasilitasKamars.get(i).equals(fasilitas.get(getAdapterPosition()))) {
                            fasilitasKamars.remove(i);
                        }
                    }
                    check.setVisibility(View.GONE);
                    boxItem.setBackgroundColor(Color.WHITE);
                }
                if (onItemClickListener != null) onItemClickListener.onItemClick(view, fasilitasKamars);
            });
        }
    }
}
