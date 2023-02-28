package com.sibkelompoke.kost.adapter;

import static com.sibkelompoke.kost.util.KostKonstan.DEFAULT_IMG_URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.Kost;

import java.util.ArrayList;

public class KostAdapter extends RecyclerView.Adapter<KostAdapter.KostViewHolder> {

    private OnItemClickListener onClickListener;

    private final Context context;
    private final ArrayList<Kost> kosts;

    public KostAdapter (Context context, ArrayList<Kost> kosts) {
        this.context = context;
        this.kosts = kosts;
    }

    @NonNull
    @Override
    public KostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_layout, parent, false);
        return new KostViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull KostViewHolder holder, int position) {
        holder.namaKost.setText(kosts.get(position).getNamaKost());
        holder.waktuBukaKost.setText("waktu buka kost : " + kosts.get(position).getWaktuBukaKost() + " Wita");
        holder.tipeKost.setText(kosts.get(position).getTipeKost());
        holder.hargaKamar.setText("Rp." + kosts.get(position).getHarga() + " /Bulan");
        holder.alamatKost.setText(kosts.get(position).getAlamat().getJalan() + ", " + kosts.get(position).getAlamat().getKelurahan());

        if (kosts.get(position).getImageUrl().size() > 0) {
            Glide.with(context).load(kosts.get(position).getImageUrl().get(0)).into(holder.kostImage);
        } else {
            Glide.with(context).load(DEFAULT_IMG_URL)
                    .into(holder.kostImage);
        }
    }

    @Override
    public int getItemCount() {
        return kosts.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Kost kost);
    }

    public void setOnClickListener(OnItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class KostViewHolder extends RecyclerView.ViewHolder {
        TextView namaKost, waktuBukaKost, tipeKost, hargaKamar, alamatKost;
        ImageView kostImage;

        public KostViewHolder(@NonNull View itemView) {
            super(itemView);

            namaKost = itemView.findViewById(R.id.tvNamaKost);
            waktuBukaKost = itemView.findViewById(R.id.tvJamBukaKost);
            tipeKost = itemView.findViewById(R.id.tvTipeKost);
            kostImage = itemView.findViewById(R.id.kostImage);
            hargaKamar = itemView.findViewById(R.id.hargaKamar);
            alamatKost = itemView.findViewById(R.id.alamatKost);

            itemView.setOnClickListener(view -> {
                if (onClickListener != null) {
                    try {
                        onClickListener.onItemClick(itemView, kosts.get(getAdapterPosition()));
                    } catch (IndexOutOfBoundsException e) {
                        Toast.makeText(context, "Maaf data tidak ditemukan. silahkan coba lagi nanti", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
