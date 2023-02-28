package com.sibkelompoke.kost.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.sibkelompoke.kost.model.Kost;

import java.util.ArrayList;

public class KostAdapter2 extends RecyclerView.Adapter<KostAdapter2.KostViewHolder2> {
    private Context context;
    private ArrayList<Kost> kosts;

    private OnItemClickListener onItemClickListener;

    public KostAdapter2(Context context, ArrayList<Kost> kosts) {
        this.context = context;
        this.kosts = kosts;
    }

    @NonNull
    @Override
    public KostViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_layout2, parent, false);
        return new KostViewHolder2(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull KostViewHolder2 holder, int position) {
        holder.namaKost.setText(kosts.get(position).getNamaKost());
        holder.tipeKost.setText(kosts.get(position).getTipeKost());
        holder.waktu.setText("Waktu buka kost " + kosts.get(position).getWaktuBukaKost());
        holder.harga.setText("Rp." + kosts.get(position).getWaktuBukaKost() + " / Bulan");
        holder.alamat.setText(kosts.get(position).getAlamat().getJalan() + ", " + kosts.get(position).getAlamat().getKelurahan());

        Glide.with(context).load(kosts.get(position).getImageUrl().get(0)).into(holder.kostImage);
    }

    @Override
    public int getItemCount() {
        return kosts.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View view, Kost kost);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class KostViewHolder2 extends RecyclerView.ViewHolder {
        ImageView kostImage;
        TextView namaKost, tipeKost, waktu, harga, alamat;

        public KostViewHolder2(@NonNull View itemView) {
            super(itemView);
            kostImage = itemView.findViewById(R.id.kostImage);
            namaKost = itemView.findViewById(R.id.namaKost);
            tipeKost = itemView.findViewById(R.id.tipeKost);
            waktu = itemView.findViewById(R.id.waktuBukaKost);
            harga = itemView.findViewById(R.id.harga);
            alamat = itemView.findViewById(R.id.lokasi);

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(itemView, kosts.get(getAdapterPosition()));
            });
        }
    }
}
