package com.sibkelompoke.kost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.FasilitasKamar;

import java.util.ArrayList;

public class FasilitasAdapter extends RecyclerView.Adapter<FasilitasAdapter.FasilitasViewHolder> {
    private Context context;
    private ArrayList<FasilitasKamar> fasilitas;

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

    public class FasilitasViewHolder extends RecyclerView.ViewHolder {
        ImageView fasilitasImg;
        TextView fasilitasName;

        public FasilitasViewHolder(View view) {
            super(view);

            fasilitasImg = view.findViewById(R.id.fasilitasImg);
            fasilitasName = view.findViewById(R.id.fasilitasName);
        }
    }
}
